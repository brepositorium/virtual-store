package com.acme.virtualstore.service;

import com.acme.virtualstore.dto.ProductDTO;

public interface ProductService {

    ProductDTO getProduct(Long id) throws Exception;
    ProductDTO createProduct(ProductDTO productDTO);
    void deleteProduct(Long id) throws Exception;

}
