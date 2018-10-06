package com.weixin.sell.service.impl;

import com.weixin.sell.dto.OrderDTO;
import com.weixin.sell.service.OrderService;
import com.weixin.sell.service.PayService;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest
@Slf4j
public class PayServiceImplTest {

    @Autowired
    private PayService payService;

    @Autowired
    private OrderService orderService;

    @Test
    public void create() throws Exception {
        OrderDTO orderDTO = orderService.findOne("1538614862923997619");

        payService.create(orderDTO);

    }

    @Test
    public void refund(){
        OrderDTO orderDTO = orderService.findOne("1538765973656932962");

        payService.refund(orderDTO);

    }

}