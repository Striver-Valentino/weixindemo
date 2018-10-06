package com.weixin.sell.repository;

import com.weixin.sell.dataobject.OrderDetail;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface OrderDetailRepository extends JpaRepository<OrderDetail,String> {

    /**
     * 根据 订单 id 查找 订单详情。
     *
     * orderId 从 OrderMaster 里 得到，一个 订单主表 包含 多个 订单详情；
     * 因为 一个人 一次 可以 买多个 东西，然后 一起支付 ，只是一个 订单，但有 多条 订单详情。
     * 因为 一个人买的 东西 一般 不会 特别多，所以 不用 分页。
     *
     * @param orderId
     * @return
     */
    List<OrderDetail> findByOrderId(String orderId);



}
