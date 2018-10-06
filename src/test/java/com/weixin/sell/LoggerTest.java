package com.weixin.sell;

import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest
@Slf4j // 有了这个 注解，就不用 每次 都 用 LoggerFactory 生成 日志对象 了
//@Data // 与 @Slf4j 效果一样
public class LoggerTest {

    //private final Logger logger = LoggerFactory.getLogger(LoggerTest.class); // 传入 类名，打印日志的 时候 也会 打印 传入 的类名，方便 定位 相关信息。所以 一定要传入 当前的类。

    @Test
    public void test1(){

        /**
         * 日志级别：
         *
         *   ERROR(40, "ERROR"),
         *   WARN(30, "WARN"),
         *   INFO(20, "INFO"),
         *   DEBUG(10, "DEBUG"),
         *   TRACE(0, "TRACE");
         */

        /*logger.debug("debug..."); // 系统默认的日志级别是 info ，info 以上的 才会打印，debug 处于 info 以下 ，不打印。
        logger.info("info...");
        logger.error("error...");*/

        String name = "imooc";
        String password = "123456";

        log.debug("debug...");
        log.info("info...");
        log.info("name: " + name + " ,password: " + password);
        log.info("name: {}, password: {}",name,password);
        log.error("error...");

        log.warn("warn...");
    }










}
