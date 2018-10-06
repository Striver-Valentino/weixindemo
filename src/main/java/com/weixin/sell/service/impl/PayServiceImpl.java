package com.weixin.sell.service.impl;

import com.weixin.sell.dto.OrderDTO;
import com.weixin.sell.enums.ResultEnum;
import com.weixin.sell.exception.SellException;
import com.weixin.sell.service.OrderService;
import com.weixin.sell.service.PayService;
import com.weixin.sell.utils.JsonUtil;
import com.weixin.sell.utils.MathUtil;
import com.lly835.bestpay.enums.BestPayTypeEnum;
import com.lly835.bestpay.model.PayRequest;
import com.lly835.bestpay.model.PayResponse;
import com.lly835.bestpay.model.RefundRequest;
import com.lly835.bestpay.model.RefundResponse;
import com.lly835.bestpay.service.impl.BestPayServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class PayServiceImpl implements PayService {

    private static final String ORDER_NAME = "微信点餐订单";

    @Autowired
    private BestPayServiceImpl bestPayService;

    @Autowired
    private OrderService orderService;

    @Override
    public PayResponse create(OrderDTO orderDTO) {
        PayRequest payRequest = new PayRequest();
        payRequest.setOpenid(orderDTO.getBuyerOpenid());
        payRequest.setOrderAmount(orderDTO.getOrderAmount().doubleValue());
        payRequest.setOrderId(orderDTO.getOrderId());
        payRequest.setOrderName(ORDER_NAME);
        payRequest.setPayTypeEnum(BestPayTypeEnum.WXPAY_H5);

        log.info("【微信支付】发起支付， request={}", JsonUtil.toJson(payRequest));

        PayResponse payResponse = bestPayService.pay(payRequest);
        log.info("【微信支付】发起支付， response={}", JsonUtil.toJson(payResponse));

        return payResponse;
    }

    // 异步通知 支付结果
    @Override
    public PayResponse notify(String notifyData) {

        /**
         * 异步通知 注意事项：
         * 1.验证签名（是否真的微信发过来的）
         * 2.支付的状态（不一定都是支付成功）
         * 3.支付金额（一些 系统 bug有可能导致 订单金额 和 支付金额 不一样）
         * 4.支付人（下单人 == 支付人 ？ ）（有时 必须本人 支付）
         */

        // bestPayService 已经 做了 验证签名 和 验证支付的状态
        PayResponse payResponse = bestPayService.asyncNotify(notifyData); // asyncNotify 处理 异步通知 的 方法

        log.info("【微信支付】异步通知，payResponse={}", JsonUtil.toJson(payResponse));

        // 查询订单
        OrderDTO orderDTO = orderService.findOne(payResponse.getOrderId());// 先查出来
        // 判断订单是否存在
        if(orderDTO == null){
            log.error("【微信支付】异步通知，订单不存在，orderId={}",payResponse.getOrderId());
            throw new SellException(ResultEnum.ORDER_NOT_EXIST);
        }

        // 判断金额是否一致
        // 注意：payResponse.getOrderAmount() 是 double 类型，类型不一致 ，不能比较。
        // 但是 double 转 BigDecimal ，精度 还有问题，实际上 payResponse.getOrderAmount() 转 BigDecimal 后 是 0.010000000005478998 ，还是不等。所以需要规定 一个 精度 。
        //if(orderDTO.getOrderAmount().compareTo(new BigDecimal(payResponse.getOrderAmount())) != 0) // 不等于0 ，就是不相等
        if(!MathUtil.equals(payResponse.getOrderAmount(),orderDTO.getOrderAmount().doubleValue()))
        {
            log.error("【微信支付】异步通知，订单金额不一致，orderId={}，微信通知金额={}，系统金额={}",
                    payResponse.getOrderId(),
                    payResponse.getOrderAmount(),
                    orderDTO.getOrderAmount());
            throw new SellException(ResultEnum.WXPAY_NOTIFY_MONEY_VERIFY_ERROR);
        }

        // 本业务 对 支付人 没有要求，就不做 校验了

        // 所有校验通过，才去 修改订单的支付状态
        orderService.paid(orderDTO);

        return payResponse;
    }

    /**
     * 退款
     * @param orderDTO
     */
    @Override
    public RefundResponse refund(OrderDTO orderDTO) {
        RefundRequest refundRequest = new RefundRequest();
        refundRequest.setOrderId(orderDTO.getOrderId());
        refundRequest.setOrderAmount(orderDTO.getOrderAmount().doubleValue());
        refundRequest.setPayTypeEnum(BestPayTypeEnum.WXPAY_H5);

        log.info("【微信退款】request={}", JsonUtil.toJson(refundRequest));

        RefundResponse refundResponse = bestPayService.refund(refundRequest);
        log.info("【微信退款】response={}", JsonUtil.toJson(refundResponse));

        return refundResponse;
    }
}
