package com.acme.virtualstore.controller;

import com.acme.virtualstore.dto.ProductDTO;
import com.acme.virtualstore.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/products")
public class ProductController {

    private final ProductService productService;

    @Autowired
    public ProductController(ProductService productService) {
        this.productService = productService;
    }

    @GetMapping("/{id}")
    public ResponseEntity<ProductDTO> getProduct(@PathVariable Long id) throws Exception {
        ProductDTO productDTO = productService.getProduct(id);
        return ResponseEntity.ok(productDTO);
    }

}
