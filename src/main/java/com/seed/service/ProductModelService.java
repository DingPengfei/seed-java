package com.seed.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.seed.entity.ProductModel;

import java.util.List;

/**
* @author ding
* @description 针对表【product_model(产品型号)】的数据库操作Service
* @createDate 2025-05-09 15:52:38
*/
public interface ProductModelService extends IService<ProductModel> {
    
    /**
     * Get all active product models
     *
     * @return List of active product models
     */
    List<ProductModel> getAllActiveModels();
}
