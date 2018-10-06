package com.weixin.sell.utils.serializer;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;

import java.io.IOException;
import java.util.Date;

/**
 * 前端 要求 时间格式 是返回 时间戳，单位 秒， 但是 @RestController 注解 把 时间 转成 json 后 ，默认是 毫秒 单位，所以需要 转换。
 */
public class Date2LongSerializer extends JsonSerializer<Date> { // 泛型 是 Date ，就是 只对 Date 转换

    @Override
    public void serialize(Date date, JsonGenerator jsonGenerator, SerializerProvider serializerProvider) throws IOException, JsonProcessingException {

        jsonGenerator.writeNumber(date.getTime() / 1000); // 毫秒  除以 1000 ，就是 秒

    }
}
