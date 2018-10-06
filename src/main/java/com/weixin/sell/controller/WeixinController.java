package com.weixin.sell.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

@RestController
@RequestMapping("/weixin")
@Slf4j
public class WeixinController {

    @GetMapping("/auth")
    public void auth(@RequestParam("code") String code){
        log.info("进入auth方法...");
        log.info("code={}",code);

        String url = "https://api.weixin.qq.com/sns/oauth2/access_token?appid=wxa2b55daee50dc2ce&secret=5fa4d6dc4e3bc48e4def7d31043a9779&code=" + code + "&grant_type=authorization_code";
        // Spring模拟HTTP请求——RestTemplate类
        RestTemplate restTemplate = new RestTemplate();
        String response = restTemplate.getForObject(url, String.class); // getForObject() 发送一个HTTP GET请求，返回的请求体将映射为一个对象

        log.info("response={}", response);

    }















}
