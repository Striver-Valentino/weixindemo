package com.weixin.sell.converter;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.weixin.sell.dataobject.OrderDetail;
import com.weixin.sell.dto.OrderDTO;
import com.weixin.sell.enums.ResultEnum;
import com.weixin.sell.exception.SellException;
import com.weixin.sell.form.OrderForm;
import lombok.extern.slf4j.Slf4j;

import java.util.ArrayList;
import java.util.List;

@Slf4j
public class OrderForm2OrderDTOConverter {

    public static OrderDTO convert(OrderForm orderForm){
        Gson gson = new Gson();

        OrderDTO orderDTO = new OrderDTO();

        // 字段名 不同，不能用 BeanUtils
        orderDTO.setBuyerName(orderForm.getName());
        orderDTO.setBuyerPhone(orderForm.getPhone());
        orderDTO.setBuyerAddress(orderForm.getAddress());
        orderDTO.setBuyerOpenid(orderForm.getOpenid());

        List<OrderDetail> orderDetailList = new ArrayList<>();
        try {
                               // json 转成 对象
            orderDetailList = gson.fromJson(orderForm.getItems(),    // 待转换的 json （json 也是一种 String）
                                           new TypeToken<List<OrderDetail>>(){}.getType());  // 目标对象 的 Type （List<OrderDetail>  的 Type）
        } catch (Exception e) {
            log.info("【对象转换】错误, String={}", orderForm.getItems());
            throw new SellException(ResultEnum.PARAM_ERROR);
        }

        orderDTO.setOrderDetailList(orderDetailList);

        return orderDTO;
    }




}
