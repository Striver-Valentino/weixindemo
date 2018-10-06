package com.weixin.sell.controller;

import com.weixin.sell.enums.ResultEnum;
import com.weixin.sell.exception.SellException;
import lombok.extern.slf4j.Slf4j;
import me.chanjar.weixin.common.api.WxConsts;
import me.chanjar.weixin.common.exception.WxErrorException;
import me.chanjar.weixin.mp.api.WxMpService;
import me.chanjar.weixin.mp.bean.result.WxMpOAuth2AccessToken;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.net.URLEncoder;

@Controller
@RequestMapping("/wechat")
@Slf4j
public class WechatController {

    @Autowired
    private WxMpService wxMpService; // 已经在 WechatMpConfig 里 配置好

    @GetMapping("/authorize")
    public String authorize(@RequestParam("returnUrl") String returnUrl){

        // WxMpService 第三方 工具
        //WxMpService wxMpService = new WxMpServiceImpl();

        // 1 .配置（统一在 WechatMpConfig 里写）

        // 2. 调用方法
        String url = "http://sell1005.natapp1.cc/sell/wechat/userInfo"; // 要 跳转 的链接
        // redirectUrl 是由 上面的 url ，加上了 state 等参数 组成的 新 的 url 。
        String redirectUrl = wxMpService.oauth2buildAuthorizationUrl(url, WxConsts.OAUTH2_SCOPE_USER_INFO, URLEncoder.encode(returnUrl)); // 第三个 参数 是 state ，重定向到 redirectUrl 时，也会带上；因为 returnUrl 是一个 链接，所以做一下 转码 。

        log.info("【微信网页授权】获取code, redirectUrl={}",redirectUrl);
        // redirectUrl=https://open.weixin.qq.com/connect/oauth2/authorize?appid=wxa2b55daee50dc2ce&redirect_uri=http%3A%2F%2Fsell1005.natapp1.cc%2Fsell%2Fwechat%2FuserInfo&response_type=code&scope=snsapi_userinfo&state=http%3A%2F%2Fwww.imooc.com#wechat_redirect

        return "redirect:" + redirectUrl; // 重定向 ,访问 redirectUrl 之后 会获得 code ，并且由 上面定义的 url 带着 再 跳转 。（即 http://sell1005.natapp1.cc/sell/wechat/userInfo?code=XXX&&state=XXX...）
    }

    @GetMapping("/userInfo")
    public String userInfo(@RequestParam("code") String code,
                         @RequestParam("state") String returnUrl){ // redirectUrl 带过来的 returnUrl

        WxMpOAuth2AccessToken wxMpOAuth2AccessToken = new WxMpOAuth2AccessToken();

        try {
            wxMpOAuth2AccessToken = wxMpService.oauth2getAccessToken(code); // 用 code获得access token，其中也包含用户的openid等信息
        } catch (WxErrorException e) {
            log.error("【微信网页授权】{}",e);
            throw new SellException(ResultEnum.WECHAT_MP_ERROR.getCode(), e.getError().getErrorMsg());
        }

        String openId = wxMpOAuth2AccessToken.getOpenId();

        return "redirect:" + returnUrl + "?openid=" + openId; // 跳转 格式 是由 自己 的 API 文档 约定好的。

    }







}
