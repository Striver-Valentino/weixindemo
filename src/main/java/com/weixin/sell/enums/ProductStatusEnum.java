package com.weixin.sell.enums;

import lombok.Getter;

/**
 * 商品状态 枚举； 代码里 要写 很多 0、1 等 状态 时，可以用 枚举，这样 更容易 分清楚 到底是 什么 状态。
 */
@Getter
public enum ProductStatusEnum {
    UP(0,"在架"),
    DOWN(1,"下架")
    ;

    private Integer code;

    private String message;

    ProductStatusEnum(Integer code, String message) {
        this.code = code;
        this.message = message;
    }
}
