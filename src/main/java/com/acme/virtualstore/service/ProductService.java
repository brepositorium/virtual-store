package com.acme.virtualstore.service;

import com.acme.virtualstore.dto.ProductDTO;

public interface ProductService {

    ProductDTO getProduct(Long id);
    ProductDTO createProduct(ProductDTO productDTO);
    void deleteProduct(Long id);

}
