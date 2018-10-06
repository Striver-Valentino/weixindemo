package com.weixin.sell.controller;

import com.weixin.sell.VO.ProductInfoVO;
import com.weixin.sell.VO.ProductVO;
import com.weixin.sell.dataobject.ProductCategory;
import com.weixin.sell.dataobject.ProductInfo;
import com.weixin.sell.service.CategoryService;
import com.weixin.sell.service.ProductService;
import com.weixin.sell.utils.ResultVOUtil;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;

/**
 * 买家商品 API
 */
@RestController// Controller 与 json 格式
@RequestMapping("/buyer/product")
public class BuyerProductController {

    @Autowired
    private ProductService productService;

    @Autowired
    private CategoryService categoryService;



    @GetMapping("/list") // get请求 的 url
    public Object list() {
        // 从数据库 读取，再传到 页面展示：
        // 1.查询所有的上架商品（买家端 没有分页）
        List<ProductInfo> productInfoList = productService.findUpAll();

        // 2.查询类目（一次性查询）（只查 上架商品 所在的类目，其它的 不查）
        List<Integer> categoryTypeList = new ArrayList<>();

        // 传统方法
        for (ProductInfo productInfo : productInfoList) {
            categoryTypeList.add(productInfo.getCategoryType());
        }

        // 精简（java8，lambda）
        //categoryTypeList = productInfoList.stream().map(e -> e.getCategoryType()).collect(Collectors.toList());

        List<ProductCategory> productCategoryList = categoryService.findByCategoryTypeIn(categoryTypeList);

        // 3.数据拼装（拼装 成 前端API 要求 的 json）
        List<ProductVO> productVOList = new ArrayList<>();
        for (ProductCategory productCategory : productCategoryList) {
            ProductVO productVO = new ProductVO();
            productVO.setCategoryType(productCategory.getCategoryType());
            productVO.setCategoryName(productCategory.getCategoryName());

            List<ProductInfoVO> productInfoVOList = new ArrayList<>();
            for (ProductInfo productInfo : productInfoList) {
                if(productInfo.getCategoryType().equals(productCategory.getCategoryType())){
                    ProductInfoVO productInfoVO = new ProductInfoVO();
                    BeanUtils.copyProperties(productInfo,productInfoVO); // 拷贝属性 ，productInfoVO 里 没有的属性 就不管
                    productInfoVOList.add(productInfoVO);
                }
            }

            productVO.setProductInfoVOList(productInfoVOList);

            productVOList.add(productVO);
        }


        /*ResultVO resultVO = new ResultVO();
        resultVO.setCode(0);
        resultVO.setMsg("成功");

        resultVO.setData(productVOList);*/


        /*ProductVO productVO = new ProductVO();
        ProductInfoVO productInfoVO = new ProductInfoVO();

        productVO.setProductInfoVOList(Arrays.asList(productInfoVO));

        resultVO.setData(Arrays.asList(productVO));*/


        //return resultVO; // 要返回 json格式 的 对象，必须要有 get、set 方法 ，否则 会 报错。

        return ResultVOUtil.success(productVOList);
    }
















}
