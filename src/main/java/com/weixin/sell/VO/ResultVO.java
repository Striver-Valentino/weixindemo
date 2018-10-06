package com.weixin.sell.VO;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;

/**
 * http 请求返回的最外层对象
 */
@Data
//@JsonInclude(JsonInclude.Include.NON_NULL) // 注明，在 序列化 为 json 时，只 序列化 不为 null 的属性。（可以在配置文件里 配置全局）
public class ResultVO<T> {

    /** 错误码. */
    private Integer code; // 用来 标明 是否请求 成功

    /** 提示信息. */
    private String msg; //= "";  // 如果 msg 是 前端必须返回的字段，但是又不能 为 null ，
                                // 并且 暂时没有值，那么 就 应该 在类里 先给 一个 初始值

    /** 返回页面 的 具体内容. */
    private T data; // 因为 返回的 页面数据 ，类型 待定（目前只知道 是一个对象），所以 用 泛型





}
