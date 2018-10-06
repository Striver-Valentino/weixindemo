package com.weixin.sell.VO;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.util.List;

/**
 * 商品（包含类目），返回 页面显示
 */
@Data
public class ProductVO {

    @JsonProperty("name")
    //private String name;
    private String categoryName; // 这里 是 类目 名，但是 前端 API 要求 返回类目名叫 name，这就 很容易 混淆，不知道是指 哪个 名称，
                                 // 所以 这里 起一个 容易 分辨 的 变量名 categoryName，同时为了 符合 前端 API，加上 @JsonProperty("name") ，
                                 // 这表示 在 对象 序列化 为 json 时，categoryName 改为 name 。

    @JsonProperty("type")
    //private Integer type;
    private Integer categoryType;

    @JsonProperty("foods")
    private List<ProductInfoVO> productInfoVOList;



}
