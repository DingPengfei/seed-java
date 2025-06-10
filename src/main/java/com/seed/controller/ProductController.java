package com.seed.controller;

import com.seed.common.web.AjaxResult;
import com.seed.entity.Product;
import com.seed.service.ProductService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * Product controller for managing products
 */
@RestController
@RequestMapping("/api/product")
@Tag(name = "Product Management", description = "APIs for managing products")
public class ProductController {
    
    private static final Logger log = LoggerFactory.getLogger(ProductController.class);
    
    @Autowired
    private ProductService productService;
    
    /**
     * Get all products belonging to a user's space by user code
     * 
     * @param userCode The user code to query products for
     * @return AjaxResult containing list of products in the user's space
     */
    @GetMapping("/list-by-user")
    @Operation(
        summary = "List Products by User",
        description = "Retrieves all products belonging to a user's space identified by the user code"
    )
    public AjaxResult listProductsByUserCode(
        @Parameter(description = "The unique code of the user") 
        @RequestParam("userCode") String userCode
    ) {
        try {
            log.info("Listing products for user with code: {}", userCode);
            
            if (userCode == null || userCode.trim().isEmpty()) {
                return AjaxResult.error("用户代码不能为空");
            }
            
            List<Product> products = productService.findProductsByUserCode(userCode);
            return AjaxResult.success("查询成功", products);
            
        } catch (Exception e) {
            log.error("Failed to list products for user code: {}", userCode, e);
            return AjaxResult.error("查询产品失败：" + e.getMessage());
        }
    }
}
