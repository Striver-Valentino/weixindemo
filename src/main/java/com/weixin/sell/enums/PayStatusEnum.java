package com.weixin.sell.enums;

import lombok.Getter;

/**
 * 支付状态
 */
@Getter
public enum PayStatusEnum {

    WAIT(0,"等待支付"),
    SUCCESS(1,"支付成功"), // 没有 支付 出错 状态，因为 如果 支付出错，是不可能 把 状态 0 改变的，还是 待支付 。
    ;


    private Integer code;

    private String message;

    PayStatusEnum(Integer code, String message) {
        this.code = code;
        this.message = message;
    }
}
