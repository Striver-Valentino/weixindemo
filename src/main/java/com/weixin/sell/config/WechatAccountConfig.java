package com.weixin.sell.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * 微信账号 相关 配置
 */
@Data
@Component
@ConfigurationProperties(prefix = "wechat") // @ConfigurationProperties主要作用：就是绑定application.properties 或 application.yml 中的属性
                                            // prefix = "wechat" ，是指 绑定 application.yml 里 以 wechat 开头 的 属性。
public class WechatAccountConfig {

    private String mpAppId; // 值为  application.yml 里 ，wechat.mpAppId

    private String mpAppSecret; // 值为  application.yml 里 ，wechat.mpAppSecret

    /**
     * 商户号
     */
    private String mchId;

    /**
     * 商户密钥
     */
    private String mchKey;

    /**
     * 商户证书路径
     */
    private String keyPath;

    /**
     * 微信支付异步通知地址
     */
    private String notifyUrl;













}
