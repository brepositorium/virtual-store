package com.acme.virtualstore.service;

import com.acme.virtualstore.dto.ProductDTO;
import com.acme.virtualstore.entity.Product;
import com.acme.virtualstore.exception.ResourceNotFoundException;
import com.acme.virtualstore.repository.ProductRepository;
import com.acme.virtualstore.service.impl.ProductServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class ProductServiceImplTest {

    @Mock
    private ProductRepository productRepository;

    @InjectMocks
    private ProductServiceImpl productService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void getProduct_ExistingId_ReturnsProductDTO() {
        // Arrange
        Long id = 1L;
        Product product = new Product();
        product.setId(id);
        product.setName("Test Product");
        product.setItemsOnStock(10);
        product.setItemsSold(5);

        when(productRepository.findById(id)).thenReturn(Optional.of(product));

        // Act
        ProductDTO result = productService.getProduct(id);

        // Assert
        assertNotNull(result);
        assertEquals(id, result.getId());
        assertEquals("Test Product", result.getName());
        assertEquals(10, result.getItemsOnStock());
        assertEquals(5, result.getItemsSold());
    }

    @Test
    void getProduct_NonExistingId_ThrowsResourceNotFoundException() {
        // Arrange
        Long id = 1L;
        when(productRepository.findById(id)).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(ResourceNotFoundException.class, () -> productService.getProduct(id));
    }

    @Test
    void deleteProduct_ExistingId_DeletesProduct() {
        // Arrange
        Long id = 1L;
        when(productRepository.existsById(id)).thenReturn(true);

        // Act
        productService.deleteProduct(id);

        // Assert
        verify(productRepository).deleteById(id);
    }

    @Test
    void deleteProduct_NonExistingId_ThrowsResourceNotFoundException() {
        // Arrange
        Long id = 1L;
        when(productRepository.existsById(id)).thenReturn(false);

        // Act & Assert
        assertThrows(ResourceNotFoundException.class, () -> productService.deleteProduct(id));
        verify(productRepository, never()).deleteById(any());
    }
}
