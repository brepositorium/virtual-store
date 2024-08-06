package com.acme.virtualstore.service.impl;

import com.acme.virtualstore.dto.ProductDTO;
import com.acme.virtualstore.entity.Product;
import com.acme.virtualstore.repository.ProductRepository;
import com.acme.virtualstore.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ProductServiceImpl implements ProductService {
    private final ProductRepository productRepository;

    @Autowired
    public ProductServiceImpl(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }

    @Override
    public ProductDTO getProduct(Long id) throws Exception {
        Product product = productRepository.findById(id)
                .orElseThrow(() -> new Exception("Product not found"));
        return convertToDTO(product);
    }

    @Override
    public ProductDTO createProduct(ProductDTO productDTO) {
        Product product = convertToEntity(productDTO);
        Product savedProduct = productRepository.save(product);
        return convertToDTO(savedProduct);
    }

    @Override
    public void deleteProduct(Long id) throws Exception {
        if (!productRepository.existsById(id)) {
            throw new Exception("Product not found");
        }
        productRepository.deleteById(id);
    }

    private ProductDTO convertToDTO(Product product) {
        return ProductDTO.builder()
                .id(product.getId())
                .name(product.getName())
                .itemsOnStock(product.getItemsOnStock())
                .itemsSold(product.getItemsSold())
                .build();
    }

    private Product convertToEntity(ProductDTO productDTO) {
        Product product = new Product();
        product.setId(productDTO.getId());
        product.setName(productDTO.getName());
        product.setItemsOnStock(productDTO.getItemsOnStock());
        product.setItemsSold(productDTO.getItemsSold());
        return product;

    }
}
