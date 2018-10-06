package com.weixin.sell.VO;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.math.BigDecimal;

/**
 * 商品详情，返回前端
 *
 * 这个 ProductInfoVO 和 ProductInfo 很像，但是 不能 直接 用 ProductInfo 代替 ProductInfoVO ，
 * 因为 前端 API 并不需要 那么多的 商品详情 信息，如果 返回 ProductInfo ，那么 库存 等 商家 隐私 信息
 * 都返回 前端了，这不安全。
 * 前端 API 要求什么，就返回什么，封装在 ProductInfoVO 就行了 。
 */
@Data
public class ProductInfoVO {

    @JsonProperty("id")
    private String productId;

    @JsonProperty("name")
    private String productName;

    @JsonProperty("price")
    private BigDecimal productPrice;

    @JsonProperty("description")
    private String productDescription;

    @JsonProperty("icon")
    private String productIcon;

}
