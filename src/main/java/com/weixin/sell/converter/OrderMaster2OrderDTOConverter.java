package com.weixin.sell.converter;

import com.weixin.sell.dataobject.OrderMaster;
import com.weixin.sell.dto.OrderDTO;
import org.springframework.beans.BeanUtils;

import java.util.List;
import java.util.stream.Collectors;

/**
 * OrderMaster 转 OrderDTO
 */
public class OrderMaster2OrderDTOConverter {

    // 单个转换
    public static OrderDTO convert(OrderMaster orderMaster){
        OrderDTO orderDTO = new OrderDTO();

        BeanUtils.copyProperties(orderMaster,orderDTO);

        return orderDTO;
    }

    // list 转换
    public static List<OrderDTO> convert(List<OrderMaster> orderMasterList){
        List<OrderDTO> orderDTOList = orderMasterList.stream().map(e -> convert(e)).collect(Collectors.toList());

        return orderDTOList;
    }

}
