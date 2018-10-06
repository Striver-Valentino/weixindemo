package com.weixin.sell.repository;

import com.weixin.sell.dataobject.OrderMaster;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OrderMasterRepository extends JpaRepository<OrderMaster,String> {

    /**
     * 根据 买家 openid 分页查找。
     *
     * 不加 Pageable ，就是普通 的查找：
     * List<OrderMaster> findByBuyerOpenid(String buyerOpenid);
     *
     * @param buyerOpenid
     * @param pageable
     * @return
     */
    Page<OrderMaster> findByBuyerOpenid(String buyerOpenid, Pageable pageable);








}
