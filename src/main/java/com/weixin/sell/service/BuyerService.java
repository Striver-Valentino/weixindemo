package com.weixin.sell.service;

import com.weixin.sell.dto.OrderDTO;

/**
 * 买家 Service
 */
public interface BuyerService {

    // 查询一个订单； 如果 命名 findOne，结合 接口名 BuyerService 会 以为 是 查一个 买家，为了 命名 易懂，应该叫 findOrderOne
    OrderDTO findOrderOne(String openid, String orderId);

    // 取消订单
    OrderDTO cancelOrder(String openid, String orderId);

}
