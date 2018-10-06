package com.weixin.sell.dto;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.weixin.sell.dataobject.OrderDetail;
import com.weixin.sell.utils.serializer.Date2LongSerializer;
import lombok.Data;

import javax.persistence.Id;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

/**
 * DTO：Data Transportation Object 数据传输对象，专用于 在 各个 层 之间 传输 的 对象。
 *
 */
@Data
//@JsonInclude(JsonInclude.Include.NON_NULL) // 注明，在 序列化 为 json 时，只 序列化 不为 null 的属性。（List<OrderDetail> 有时候 在前端 用不上，不用返回）
//@JsonSerialize(include = JsonSerialize.Inclusion.NON_NULL) // 与 上一 注解 效果一样
public class OrderDTO { // OrderMaster 的 数据传输对象

    /** 订单id. */
    @Id
    private String orderId;

    /** 买家名字. */
    private String buyerName;

    /** 买家手机号. */
    private String buyerPhone;

    /** 买家地址. */
    private String buyerAddress;

    /** 买家微信Openid. */
    private String buyerOpenid;

    /** 订单总金额. */
    private BigDecimal orderAmount;

    /** 订单状态, 默认为0新下单. */
    private Integer orderStatus; // 这里就不初始化了

    /** 支付状态, 默认为0未支付. */
    private Integer payStatus; // 这里就不初始化了

    // 需要 createTime 和  updateTime ，按时间排序 显示 订单
    /** 创建时间. */
    @JsonSerialize(using = Date2LongSerializer.class) // 利用 Date2LongSerializer 把 毫秒 改成 秒
    private Date createTime;

    /** 更新时间. */
    @JsonSerialize(using = Date2LongSerializer.class)
    private Date updateTime;

    private List<OrderDetail> orderDetailList; //= new ArrayList<>(); // 如果 orderDetailList 是 前端必须返回的字段，但是又不能 为 null ，
                                                                     // 并且 暂时没有值，那么 就 应该 在类里 先给 一个 初始值
}
