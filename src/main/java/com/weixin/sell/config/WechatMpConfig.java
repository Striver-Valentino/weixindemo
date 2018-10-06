package com.weixin.sell.config;

import me.chanjar.weixin.mp.api.WxMpConfigStorage;
import me.chanjar.weixin.mp.api.WxMpInMemoryConfigStorage;
import me.chanjar.weixin.mp.api.WxMpService;
import me.chanjar.weixin.mp.api.impl.WxMpServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;

/**
 * 微信公众号 配置 ，通过 @Component ，所有的配置 在项目启动时 就 配置好了。
 */
@Component // 泛指各种组件，就是说当我们的类不属于各种归类的时候（不属于@Controller、@Services等的时候），我们就可以使用@Component来标注这个类。
public class WechatMpConfig { // Mp 在微信里 是 公众号 的意思

    @Autowired
    private WechatAccountConfig accountConfig;

    @Bean // 在 WechatMpConfig 里 注入一个 Bean WxMpService ；因为是 @Bean 注解，所以 WxMpService 在 spring 容器里，其它地方 可以 @Autowired 引入。
    public WxMpService wxMpService(){
        WxMpServiceImpl wxMpService = new WxMpServiceImpl();
        wxMpService.setWxMpConfigStorage(wxMpConfigStorage()); // 设置 配置
        return wxMpService;
    }

    @Bean
    public WxMpConfigStorage wxMpConfigStorage(){
        WxMpInMemoryConfigStorage wxMpConfigStorage = new WxMpInMemoryConfigStorage();
        wxMpConfigStorage.setAppId(accountConfig.getMpAppId()); // 从配置文件获取
        wxMpConfigStorage.setSecret(accountConfig.getMpAppSecret()); // 从配置文件获取
        return wxMpConfigStorage;
    }






}
