package com.seed.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.seed.entity.Product;
import com.seed.entity.User;
import com.seed.dao.ProductMapper;
import com.seed.service.ProductService;
import com.seed.service.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.lang.reflect.Field;
import java.util.Collections;
import java.util.List;

/**
* @author ding
* @description 针对表【product(产品)】的数据库操作Service实现
* @createDate 2025-05-09 15:52:33
*/
@Service
public class ProductServiceImpl extends ServiceImpl<ProductMapper, Product>
    implements ProductService{
    
    private static final Logger log = LoggerFactory.getLogger(ProductServiceImpl.class);
    
    @Autowired
    private UserService userService;
    
    @Override
    public List<Product> findProductsByUserCode(String userCode) {
        if (userCode == null || userCode.trim().isEmpty()) {
            log.warn("Attempted to find products with null or empty user code");
            return Collections.emptyList();
        }
        
        // Find user by code
        QueryWrapper<User> userQuery = new QueryWrapper<>();
        userQuery.eq("code", userCode);
        userQuery.eq("is_deleted", "0"); // Only consider active users
        
        User user = userService.getOne(userQuery);
        if (user == null) {
            log.warn("User not found with code: {}", userCode);
            return Collections.emptyList();
        }
        
        // Get the space code from the user using reflection
        String spaceCode;
        try {
            Field field = User.class.getDeclaredField("spaceCode");
            field.setAccessible(true);
            spaceCode = (String) field.get(user);
        } catch (Exception e) {
            log.error("Error accessing spaceCode field", e);
            return Collections.emptyList();
        }
        
        if (spaceCode == null || spaceCode.trim().isEmpty()) {
            log.warn("User with code {} has no space code", userCode);
            return Collections.emptyList();
        }
        
        // Find products by space code
        QueryWrapper<Product> productQuery = new QueryWrapper<>();
        productQuery.eq("space_code", spaceCode);
        productQuery.eq("is_deleted", "0"); // Only consider active products
        
        return this.list(productQuery);
    }
}




