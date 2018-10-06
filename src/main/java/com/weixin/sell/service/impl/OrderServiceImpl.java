package com.weixin.sell.service.impl;

import com.weixin.sell.converter.OrderMaster2OrderDTOConverter;
import com.weixin.sell.dataobject.OrderDetail;
import com.weixin.sell.dataobject.OrderMaster;
import com.weixin.sell.dataobject.ProductInfo;
import com.weixin.sell.dto.CartDTO;
import com.weixin.sell.dto.OrderDTO;
import com.weixin.sell.enums.OrderStatusEnum;
import com.weixin.sell.enums.PayStatusEnum;
import com.weixin.sell.enums.ResultEnum;
import com.weixin.sell.exception.SellException;
import com.weixin.sell.repository.OrderDetailRepository;
import com.weixin.sell.repository.OrderMasterRepository;
import com.weixin.sell.service.OrderService;
import com.weixin.sell.service.PayService;
import com.weixin.sell.service.ProductService;
import com.weixin.sell.utils.KeyUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import java.math.BigDecimal;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Slf4j
public class OrderServiceImpl implements OrderService {

    @Autowired
    private ProductService productService;

    @Autowired
    private OrderDetailRepository orderDetailRepository;

    @Autowired
    private OrderMasterRepository orderMasterRepository;

    @Autowired
    private PayService payService;

    // 创建订单
    @Override
    @Transactional // 事务 ，有一个地方 抛异常，就会 回滚
    public OrderDTO create(OrderDTO orderDTO) {

        String orderId = KeyUtil.genUniqueKey(); // 首先 生成 订单id （不是 订单详情 id）

        BigDecimal orderAmount = new BigDecimal(0); // 累加，计算总价
        //BigDecimal orderAmount = new BigDecimal(BigInteger.ZERO); // 与上一行等价

        //List<CartDTO> cartDTOList = new ArrayList<>();

        // 1.查询商品（数量，价格）
        for (OrderDetail orderDetail : orderDTO.getOrderDetailList()) {
            ProductInfo productInfo = productService.findOne(orderDetail.getProductId());
            if(productInfo == null){ // 如果 要下单的 商品 不存在，则 抛异常
                throw new SellException(ResultEnum.PRODUCT_NOT_EXIST);
            }

            // 如果 都存在，就 逐一 计算 总价
            // 2.计算订单总价
            // BigDecimal 的乘法，不能直接 用 * ，要用 multiply 方法；库存 虽然是 int ，但是要与 BigDecimal 相乘，也要 转为 BigDecimal 。
            orderAmount = productInfo.getProductPrice()
                    .multiply(new BigDecimal(orderDetail.getProductQuantity()))
                    .add(orderAmount); // BigDecimal 加法 ，用 add 方法

            // 订单详情入库（注意，这里的 orderDetail 由 前端传过来，是不完整的，需要进一步 完善 才能 入库）
            orderDetail.setDetailId(KeyUtil.genUniqueKey());
            orderDetail.setOrderId(orderId);
            BeanUtils.copyProperties(productInfo,orderDetail);
            orderDetailRepository.save(orderDetail);

            //CartDTO cartDTO = new CartDTO(orderDetail.getProductId(), orderDetail.getProductQuantity());
            //cartDTOList.add(cartDTO);
        }

        // 3.写入订单数据库（OrderMaster）
        OrderMaster orderMaster = new OrderMaster();
        //orderMaster.setOrderId(orderId);
        //orderMaster.setOrderAmount(orderAmount);
        orderDTO.setOrderId(orderId);
        BeanUtils.copyProperties(orderDTO,orderMaster); // orderDTO 里 orderAmount 是null，拷贝的时候 也会 拷贝 null，所以之前的 设置 是没用的；应该把 设置 orderAmount 放后面。
        //orderMaster.setOrderId(orderId);
        orderMaster.setOrderAmount(orderAmount);

        // 因为 上面 的 属性拷贝 也把 orderStatus 和 payStatus 覆盖 为 null 了，所以 要重新设置。
        orderMaster.setOrderStatus(OrderStatusEnum.NEW.getCode());
        orderMaster.setPayStatus(PayStatusEnum.WAIT.getCode());

        orderMasterRepository.save(orderMaster);


        // 4.扣库存 （要考虑多线程）
        // 用 lambda 比 循环 快。
        List<CartDTO> cartDTOList = orderDTO.getOrderDetailList().stream().map(e ->              // orderDTO.getOrderDetailList() 是一个 集合，取 这个集合 的流，流 里面每个元素 e
                                        new CartDTO(e.getProductId(),e.getProductQuantity())     // 都取一部分属性 创建 一个 CartDTO 对象 ，这些 CartDTO 对象 全部收集 成 list ，
                                    ).collect(Collectors.toList());                              // 就是 CartDTO 的 list （Collectors.toList()）

        productService.decreaseStock(cartDTOList);



        return orderDTO;
    }

    @Override
    public OrderDTO findOne(String orderId) {
        OrderMaster orderMaster = orderMasterRepository.findOne(orderId);
        if(orderMaster == null){
            throw new SellException(ResultEnum.ORDER_NOT_EXIST);
        }

        List<OrderDetail> orderDetailList = orderDetailRepository.findByOrderId(orderId);
        if(CollectionUtils.isEmpty(orderDetailList)){
            throw new SellException(ResultEnum.ORDERDETAIL_NOT_EXIST);
        }

        OrderDTO orderDTO = new OrderDTO();
        BeanUtils.copyProperties(orderMaster,orderDTO);
        orderDTO.setOrderDetailList(orderDetailList);

        return orderDTO;
    }

    @Override
    public Page<OrderDTO> findList(String buyerOpenid, Pageable pageable) {
        Page<OrderMaster> orderMasterPage = orderMasterRepository.findByBuyerOpenid(buyerOpenid, pageable); // 注意，这里不用 查 订单详情


        // orderMasterPage 转 orderDTOPage
        List<OrderDTO> orderDTOList = OrderMaster2OrderDTOConverter.convert(orderMasterPage.getContent());

        Page<OrderDTO> orderDTOPage = new PageImpl<OrderDTO>(orderDTOList,pageable,orderMasterPage.getTotalElements());


        return orderDTOPage;
    }

    @Override
    @Transactional
    public OrderDTO cancel(OrderDTO orderDTO) {
        OrderMaster orderMaster = new OrderMaster();
        // BeanUtils.copyProperties(orderDTO,orderMaster);

        // 判断订单状态（如果是 已完结、已取消 的状态，不能 做 取消操作）
        if(!orderDTO.getOrderStatus().equals(OrderStatusEnum.NEW.getCode())){
            log.error("【取消订单】订单状态不正确,orderId={}, orderStatus={}",orderDTO.getOrderId(),orderDTO.getOrderStatus());
            throw new SellException(ResultEnum.ORDER_STATUS_ERROR);
        }

        // 修改订单状态
        orderDTO.setOrderStatus(OrderStatusEnum.CANCEL.getCode()); // 改为 已取消 状态

        BeanUtils.copyProperties(orderDTO,orderMaster); // 修改完 再 拷贝

        OrderMaster updateResult = orderMasterRepository.save(orderMaster); // 返回 更新后 的 对象
        if(updateResult == null){
            log.error("【取消订单】更新失败, orderMaster={}",orderMaster);
            throw new SellException(ResultEnum.ORDER_UPDATE_FAIL);
        }

        // 恢复库存
        if(CollectionUtils.isEmpty(orderDTO.getOrderDetailList())){ // 如果 订单里 没有东西，就不用 恢复库存 （这种情况 很少，但也要考虑）
            log.error("【取消订单】订单中无商品详情, orderDTO={}",orderDTO);
            throw new SellException(ResultEnum.ORDER_DETAIL_EMPTY);
        }
        List<CartDTO> cartDTOList = orderDTO.getOrderDetailList().stream().map(e -> new CartDTO(e.getProductId(), e.getProductQuantity())).collect(Collectors.toList());

        productService.increaseStock(cartDTOList);


        // 如果已支付，需要退款
        if(orderDTO.getOrderStatus().equals(PayStatusEnum.SUCCESS.getCode())){ // 如果是 支付成功 状态
            //TODO
            payService.refund(orderDTO);
        }

        return orderDTO;
    }

    @Override
    @Transactional
    public OrderDTO finish(OrderDTO orderDTO) {
        // 判断订单状态
        if(!orderDTO.getOrderStatus().equals(OrderStatusEnum.NEW.getCode())){
            log.error("【完结订单】订单状态不正确,orderId={}, orderStatus={}",orderDTO.getOrderId(),orderDTO.getOrderStatus());
            throw new SellException(ResultEnum.ORDER_STATUS_ERROR);
        }

        // 修改订单状态
        orderDTO.setOrderStatus(OrderStatusEnum.FINISHED.getCode()); // 改为 已完结 状态

        OrderMaster orderMaster = new OrderMaster();
        BeanUtils.copyProperties(orderDTO,orderMaster); // 修改完 再 拷贝

        OrderMaster updateResult = orderMasterRepository.save(orderMaster); // 返回 更新后 的 对象
        if(updateResult == null){
            log.error("【完结订单】更新失败, orderMaster={}",orderMaster);
            throw new SellException(ResultEnum.ORDER_UPDATE_FAIL);
        }

        return orderDTO;
    }

    @Override
    @Transactional
    public OrderDTO paid(OrderDTO orderDTO) {
        // 判断订单状态
        if(!orderDTO.getOrderStatus().equals(OrderStatusEnum.NEW.getCode())){
            log.error("【订单支付完成】订单状态不正确,orderId={}, orderStatus={}",orderDTO.getOrderId(),orderDTO.getOrderStatus());
            throw new SellException(ResultEnum.ORDER_STATUS_ERROR);
        }

        // 判断支付状态
        if(!orderDTO.getPayStatus().equals(PayStatusEnum.WAIT.getCode())){ // 不是 待支付 ，就 报错
            log.error("【订单支付完成】订单支付状态不正确,orderDTO={}",orderDTO);
            throw new SellException(ResultEnum.ORDER_PAY_STATUS_ERROR);
        }

        // 修改支付状态 （不是 修改订单状态）
        orderDTO.setPayStatus(PayStatusEnum.SUCCESS.getCode()); // 改为 已支付成功 状态

        OrderMaster orderMaster = new OrderMaster();
        BeanUtils.copyProperties(orderDTO,orderMaster); // 修改完 再 拷贝

        OrderMaster updateResult = orderMasterRepository.save(orderMaster); // 返回 更新后 的 对象
        if(updateResult == null){
            log.error("【订单支付完成】更新失败, orderMaster={}",orderMaster);
            throw new SellException(ResultEnum.ORDER_UPDATE_FAIL);
        }

        return orderDTO;
    }
}
