package com.weixin.sell.service;

import com.weixin.sell.dataobject.ProductInfo;
import com.weixin.sell.dto.CartDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface ProductService {

    ProductInfo findOne(String productId);

    /**
     * 查询所有 在架商品 列表 （买家端）
     * @return
     */
    List<ProductInfo> findUpAll();

    /**
     * 查询所有 商品 列表 （卖家端），因为 商品 很多，所以 需要分页，传入 Pageable
     * @param pageable
     * @return
     */
    Page<ProductInfo> findAll(Pageable pageable); // findAll 传入 Pageable 后，返回的是 Page ， 不是 list

    ProductInfo save(ProductInfo productInfo);

    /**
     * 下订单 ，减库存；取消 订单 ，加库存
     */

    // 加库存（不用返回值，出错时 抛异常 即可）
    void increaseStock(List<CartDTO> cartDTOList);

    // 减库存
    void decreaseStock(List<CartDTO> cartDTOList);

}
