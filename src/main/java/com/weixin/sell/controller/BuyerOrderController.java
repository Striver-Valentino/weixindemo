package com.weixin.sell.controller;

import com.weixin.sell.VO.ResultVO;
import com.weixin.sell.converter.OrderForm2OrderDTOConverter;
import com.weixin.sell.dto.OrderDTO;
import com.weixin.sell.enums.ResultEnum;
import com.weixin.sell.exception.SellException;
import com.weixin.sell.form.OrderForm;
import com.weixin.sell.service.BuyerService;
import com.weixin.sell.service.OrderService;
import com.weixin.sell.utils.ResultVOUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 买家 订单 Controller
 */
@RestController
@RequestMapping("/buyer/order")
@Slf4j
public class BuyerOrderController {

    @Autowired
    private OrderService orderService;

    @Autowired
    private BuyerService buyerService;

    // 创建订单
    @PostMapping("/create")
    public ResultVO<Map<String,String>> create(@Valid OrderForm orderForm,     // @Valid注解用于校验 （需要在实体类的相应字段上添加用于充当校验条件的注解，如：@NotEmpty）
                                               BindingResult bindingResult){   // 传入BindingResult对象，用于获取校验失败情况下的反馈信息

        if(bindingResult.hasErrors()){
            log.error("【创建订单】参数不正确,orderForm={}",orderForm);
            throw new SellException(ResultEnum.PARAM_ERROR.getCode(),   // 只写 PARAM_ERROR ，只知道 参数不正确，并不知道 具体哪个 参数 不正确。
                    bindingResult.getFieldError().getDefaultMessage());  // 要 加上 表单校验 结果 的 相关信息
        }

        OrderDTO orderDTO = OrderForm2OrderDTOConverter.convert(orderForm);
        if(CollectionUtils.isEmpty(orderDTO.getOrderDetailList())){
            log.error("【创建订单】购物车不能为空");
            throw new SellException(ResultEnum.CART_EMPTY);
        }
        OrderDTO createResult = orderService.create(orderDTO);

        Map<String,String> map = new HashMap<>();
        map.put("orderId",createResult.getOrderId());

        return ResultVOUtil.success(map);
    }


    // 订单列表（不查 详情）
    @GetMapping("/list")
    public ResultVO<List<OrderDTO>> list(@RequestParam("openid") String openid,
                                         @RequestParam(value = "page",defaultValue = "0") Integer page,  // 如果 不传 page ，则默认 为 0 ，保证了 代码 的 健壮性
                                         @RequestParam(value = "size",defaultValue = "10") Integer size){ // 如果 不传 size ，则默认 为 10 ，保证了 代码 的 健壮性

        if(StringUtils.isEmpty(openid)){
            log.error("【查询订单列表】openid为空");
            throw new SellException(ResultEnum.PARAM_ERROR);
        }

        PageRequest request = new PageRequest(page, size);
        Page<OrderDTO> orderDTOPage = orderService.findList(openid, request);


        return ResultVOUtil.success(orderDTOPage.getContent());
        //return ResultVOUtil.success();

        /*ResultVO resultVO = new ResultVO();
        resultVO.setCode(0);
        return resultVO;*/
    }


    // 订单详情（查询单个 订单主表 里的 每个详情）
    @GetMapping("/detail")
    public ResultVO<OrderDTO> detail(@RequestParam("openid") String openid,
                                     @RequestParam("orderId") String orderId){

        if(StringUtils.isEmpty(openid)){
            log.error("【查询订单详情】openid为空");
            throw new SellException(ResultEnum.PARAM_ERROR);
        }
        if(StringUtils.isEmpty(orderId)){
            log.error("【查询订单详情】orderId为空");
            throw new SellException(ResultEnum.PARAM_ERROR);
        }

        //TODO 不安全的做法，改进 （任何一个人 拿着 一个 别人的orderId 都可以 查别人的订单了）
 /*     OrderDTO orderDTO = orderService.findOne(orderId);
        // 要判断 openid
        if(!orderDTO.getBuyerOpenid().equals(openid)){ // 安全的做法，但是 只要涉及 openid 的 地方 都要这样写（比如，还有 取消订单 时），会很繁琐。 所以统一写在 BuyerService 里
            //......
        }    */

        OrderDTO orderDTO = buyerService.findOrderOne(openid, orderId);

        return ResultVOUtil.success(orderDTO);
    }


    // 取消订单
    @PostMapping("/cancel")
    public ResultVO cancel(@RequestParam("openid") String openid,
                                     @RequestParam("orderId") String orderId){

        if(StringUtils.isEmpty(openid)){
            log.error("【查询订单详情】openid为空");
            throw new SellException(ResultEnum.PARAM_ERROR);
        }
        if(StringUtils.isEmpty(orderId)){
            log.error("【查询订单详情】orderId为空");
            throw new SellException(ResultEnum.PARAM_ERROR);
        }

        //TODO 不安全的做法，改进
        /*OrderDTO orderDTO = orderService.findOne(orderId);

        orderService.cancel(orderDTO);*/

        buyerService.cancelOrder(openid, orderId);

        return ResultVOUtil.success();
    }










}
