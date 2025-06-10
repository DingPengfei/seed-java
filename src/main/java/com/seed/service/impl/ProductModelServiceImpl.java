package com.seed.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.seed.entity.ProductModel;
import com.seed.dao.ProductModelMapper;
import com.seed.service.ProductModelService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.List;

/**
* @author ding
* @description 针对表【product_model(产品型号)】的数据库操作Service实现
* @createDate 2025-05-09 15:52:38
*/
@Service
public class ProductModelServiceImpl extends ServiceImpl<ProductModelMapper, ProductModel>
    implements ProductModelService{
    
    private static final Logger log = LoggerFactory.getLogger(ProductModelServiceImpl.class);
    
    @Override
    public List<ProductModel> getAllActiveModels() {
        log.info("Fetching all active product models");
        
        QueryWrapper<ProductModel> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("is_deleted", "0"); // Only consider non-deleted models
        queryWrapper.orderByAsc("id");      // Order by ID for consistency
        
        return this.list(queryWrapper);
    }
}




