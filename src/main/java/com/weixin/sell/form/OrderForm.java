package com.weixin.sell.form;

import lombok.Data;
import org.hibernate.validator.constraints.NotEmpty;

/**
 * XXXForm ，前端 用于 表单验证 的 类。（即 封装 前端传入 Controller 的所有参数）
 */
@Data
public class OrderForm {

    /**
     * 买家姓名
     */
    @NotEmpty(message = "姓名必填") // spring 提供的 表单验证
    private String name;

    /**
     * 买家手机号
     */
    @NotEmpty(message = "手机号必填")
    private String phone;

    /**
     * 买家地址
     */
    @NotEmpty(message = "地址必填")
    private String address;

    /**
     * 买家微信openid
     */
    @NotEmpty(message = "openid必填")
    private String openid;

    /**
     * 购物车 （前端传入 的 ，也是一个 String）
     */
    @NotEmpty(message = "购物车不能为空")
    private String items;

}
