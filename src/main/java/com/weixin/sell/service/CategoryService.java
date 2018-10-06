package com.weixin.sell.service;

import com.weixin.sell.dataobject.ProductCategory;

import java.util.List;

public interface CategoryService {

    /**
     * 卖家端 用：
      */
    ProductCategory findOne(Integer categoryId);

    List<ProductCategory> findAll();

    ProductCategory save(ProductCategory productCategory);

    /**
     * 买家端 用：
      */
    List<ProductCategory> findByCategoryTypeIn(List<Integer> categoryTypeList);











}
