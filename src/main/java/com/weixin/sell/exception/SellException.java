package com.weixin.sell.exception;

import com.weixin.sell.enums.ResultEnum;

/**
 * 自定义下单时的异常
 */
public class SellException extends RuntimeException {

    private Integer code;

    public SellException(ResultEnum resultEnum) {
        super(resultEnum.getMessage());

        this.code = resultEnum.getCode();
    }

    public SellException(Integer code,String message) {
        super(message);

        this.code = code;
    }
}
