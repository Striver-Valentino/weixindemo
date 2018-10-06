package com.weixin.sell.dataobject;

import com.weixin.sell.enums.OrderStatusEnum;
import com.weixin.sell.enums.PayStatusEnum;
import lombok.Data;
import org.hibernate.annotations.DynamicUpdate;

import javax.persistence.Entity;
import javax.persistence.Id;
import java.math.BigDecimal;
import java.util.Date;

/**
 * 订单主表
 */
@Entity
@Data
@DynamicUpdate
public class OrderMaster {

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
    private Integer orderStatus = OrderStatusEnum.NEW.getCode();

    /** 支付状态, 默认为0未支付. */
    private Integer payStatus = PayStatusEnum.WAIT.getCode();

    // 需要 createTime 和  updateTime ，按时间排序 显示 订单
    /** 创建时间. */
    private Date createTime;

    /** 更新时间. */
    private Date updateTime;

    //@Transient
    //private List<OrderDetail> orderDetailList; // 增加这个 属性，这样 可以和 OrderDetail 关联起来，写代码 更方便。
                                               // 但是 数据库并没有 这个 字段，DAO层 的操作 会 报错，
                                               // 使用 @Transient 注解，如果 是 数据库 没有 的字段，就会忽略 。
                                               // 注解 虽然解决了 这个问题，但是 这样做 还是有点 乱，所以 最好 重新写一个 类，用于 在 各个 层 之间 传输，称为 DTO 。

}
