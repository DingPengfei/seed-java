package com.seed.controller;

import com.seed.common.web.AjaxResult;
import com.seed.entity.ProductModel;
import com.seed.service.ProductModelService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * Controller for managing product models
 */
@RestController
@RequestMapping("/api/product-model")
@Tag(name = "Product Model Management", description = "APIs for managing product models")
public class ProductModelController {
    
    private static final Logger log = LoggerFactory.getLogger(ProductModelController.class);
    
    @Autowired
    private ProductModelService productModelService;
    
    /**
     * Get all active product models
     * 
     * @return AjaxResult containing list of all active product models
     */
    @GetMapping("/list")
    @Operation(
        summary = "List Product Models",
        description = "Retrieves all active product models ordered by ID"
    )
    public AjaxResult listProductModels() {
        try {
            log.info("Listing all active product models");
            
            List<ProductModel> productModels = productModelService.getAllActiveModels();
            return AjaxResult.success("查询成功", productModels);
            
        } catch (Exception e) {
            log.error("Failed to list product models", e);
            return AjaxResult.error("查询产品型号失败：" + e.getMessage());
        }
    }
} 