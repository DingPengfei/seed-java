package com.seed.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.seed.entity.Product;

import java.util.List;

/**
* @author ding
* @description 针对表【product(产品)】的数据库操作Service
* @createDate 2025-05-09 15:52:33
*/
public interface ProductService extends IService<Product> {
    
    /**
     * Find all products within a user's space by user code
     *
     * @param userCode The unique code of the user
     * @return List of products in the user's space
     */
    List<Product> findProductsByUserCode(String userCode);
}
