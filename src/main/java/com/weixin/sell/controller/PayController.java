package com.weixin.sell.controller;

import com.weixin.sell.dto.OrderDTO;
import com.weixin.sell.enums.ResultEnum;
import com.weixin.sell.exception.SellException;
import com.weixin.sell.service.OrderService;
import com.weixin.sell.service.PayService;
import com.lly835.bestpay.model.PayResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import java.util.Map;

@Controller
@RequestMapping("/pay")
public class PayController {

    @Autowired
    private OrderService orderService;

    @Autowired
    private PayService payService;

    @GetMapping("/create")
    public ModelAndView create(@RequestParam("orderId") String orderId,
                               @RequestParam("returnUrl") String returnUrl,
                               Map<String,Object> map){

        // 1.查询订单 （不存在 就 抛异常）
        OrderDTO orderDTO = orderService.findOne(orderId);
        if(orderDTO == null){
            throw new SellException(ResultEnum.ORDER_NOT_EXIST);
        }

        // 发起支付
        PayResponse payResponse = payService.create(orderDTO);

        map.put("payResponse",payResponse);
        map.put("returnUrl",returnUrl);

        // 使用 freemarker 就返回 ModelAndView。模板文件 后缀名 .ftl ，但是 在 ModelAndView 里 只写 文件名 即可，不用 写后缀
        return new ModelAndView("pay/create",map); // map保存着 注入 模板文件 create.ftl 的数据。
    }

    // 微信异步通知 支付结果
    @PostMapping("/notify")
    public ModelAndView notify(@RequestBody String notifyData){ // 微信会传过来 一个 XML 格式的字符串

        payService.notify(notifyData);

        // 返回给微信处理结果 （如果 不返回，微信 会 一直 发送 异步通知）
        return new ModelAndView("pay/success"); // 跳转到模板
    }












}
