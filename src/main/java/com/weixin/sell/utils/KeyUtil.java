package com.weixin.sell.utils;

import java.util.Random;

/**
 * 生成 主键 的工具类
 */
public class KeyUtil {

    /**
     * 生成唯一的主键
     * 格式：时间+随机数
     * @return
     */
    public static synchronized String genUniqueKey(){ // 用 synchronized ，考虑 线程安全
        Random random = new Random();

        Integer number = random.nextInt(900000) + 100000; // 生成 6 位 随机数

        return System.currentTimeMillis() + String.valueOf(number);
    }





}
