package com.weixin.sell.service.impl;

import com.weixin.sell.dataobject.ProductInfo;
import com.weixin.sell.dto.CartDTO;
import com.weixin.sell.enums.ProductStatusEnum;
import com.weixin.sell.enums.ResultEnum;
import com.weixin.sell.exception.SellException;
import com.weixin.sell.repository.ProductInfoRepository;
import com.weixin.sell.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class ProductServiceImpl implements ProductService {

    @Autowired
    private ProductInfoRepository repository;



    @Override
    public ProductInfo findOne(String productId) {
        return repository.findOne(productId);
    }

    @Override
    public List<ProductInfo> findUpAll() {
        return repository.findByProductStatus(ProductStatusEnum.UP.getCode());
    }

    @Override
    public Page<ProductInfo> findAll(Pageable pageable) {
        return repository.findAll(pageable);
    }

    @Override
    public ProductInfo save(ProductInfo productInfo) {
        return repository.save(productInfo);
    }

    @Override
    @Transactional
    public void increaseStock(List<CartDTO> cartDTOList) {
        for (CartDTO cartDTO : cartDTOList) {
            ProductInfo productInfo = repository.findOne(cartDTO.getProductId());
            if(productInfo == null){ // 如果商品不存在
                throw new SellException(ResultEnum.PRODUCT_NOT_EXIST);
            }

            Integer result = productInfo.getProductStock() + cartDTO.getProductQuantity();
            // 增加 库存 ，不用担心 库存数量 会小于 0
            productInfo.setProductStock(result);

            repository.save(productInfo);

        }

    }

    @Override
    @Transactional
    public void decreaseStock(List<CartDTO> cartDTOList) {
        for (CartDTO cartDTO : cartDTOList) {
            ProductInfo productInfo = repository.findOne(cartDTO.getProductId());
            if(productInfo == null){ // 如果商品不存在
                throw new SellException(ResultEnum.PRODUCT_NOT_EXIST);
            }

            // 商品 原数量 - 购物车里 的 数量
            Integer result = productInfo.getProductStock() - cartDTO.getProductQuantity();
            if(result < 0){
                throw new SellException(ResultEnum.PRODUCT_STOCK_ERROR);
            }
            // 如果 库存符合 ，就 设置 新的库存。（这里应该考虑多线程，否则 可能出现 卖出去的 比 库存 还多的情况）
            productInfo.setProductStock(result);

            repository.save(productInfo);
        }

    }
}
