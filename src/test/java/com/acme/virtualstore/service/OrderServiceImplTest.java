package com.acme.virtualstore.service;

import com.acme.virtualstore.dto.OrderDTO;
import com.acme.virtualstore.dto.OrderItemDTO;
import com.acme.virtualstore.entity.Order;
import com.acme.virtualstore.entity.OrderItem;
import com.acme.virtualstore.entity.Product;
import com.acme.virtualstore.exception.ResourceNotFoundException;
import com.acme.virtualstore.repository.OrderRepository;
import com.acme.virtualstore.repository.ProductRepository;
import com.acme.virtualstore.service.impl.OrderServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class OrderServiceImplTest {

    @Mock
    private OrderRepository orderRepository;

    @Mock
    private ProductRepository productRepository;

    @InjectMocks
    private OrderServiceImpl orderService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void createOrder_ValidOrderDTO_ReturnsCreatedOrderDTOAndUpdatesInventory() {
        // Arrange
        OrderItemDTO orderItemDTO = OrderItemDTO.builder()
                .productId(1L)
                .quantity(2)
                .build();

        OrderDTO inputOrderDTO = OrderDTO.builder()
                .orderDate(LocalDateTime.now())
                .orderItems(List.of(orderItemDTO))
                .build();

        Product product = new Product();
        product.setId(1L);
        product.setItemsOnStock(5);
        product.setItemsSold(0);

        Order savedOrder = new Order();
        savedOrder.setId(1L);
        savedOrder.setOrderDate(inputOrderDTO.getOrderDate());
        OrderItem orderItem = new OrderItem();
        orderItem.setId(1L);
        orderItem.setProduct(product);
        orderItem.setQuantity(2);
        orderItem.setOrder(savedOrder);
        savedOrder.setOrderItems(List.of(orderItem));

        when(productRepository.findById(1L)).thenReturn(Optional.of(product));
        when(orderRepository.save(any(Order.class))).thenReturn(savedOrder);

        // Act
        OrderDTO result = orderService.createOrder(inputOrderDTO);

        // Assert
        assertNotNull(result);
        assertEquals(1L, result.getId());
        assertEquals(inputOrderDTO.getOrderDate(), result.getOrderDate());
        assertEquals(1, result.getOrderItems().size());
        assertEquals(1L, result.getOrderItems().get(0).getId());
        assertEquals(1L, result.getOrderItems().get(0).getProductId());
        assertEquals(2, result.getOrderItems().get(0).getQuantity());

        // Verify inventory update
        assertEquals(3, product.getItemsOnStock());
        assertEquals(2, product.getItemsSold());

        verify(productRepository).findById(1L);
        verify(productRepository).save(product);
        verify(orderRepository).save(any(Order.class));
    }

    @Test
    void createOrder_InsufficientStock_ThrowsIllegalStateException() {
        // Arrange
        OrderItemDTO orderItemDTO = OrderItemDTO.builder()
                .productId(1L)
                .quantity(10)
                .build();

        OrderDTO inputOrderDTO = OrderDTO.builder()
                .orderDate(LocalDateTime.now())
                .orderItems(List.of(orderItemDTO))
                .build();

        Product product = new Product();
        product.setId(1L);
        product.setItemsOnStock(5);
        product.setItemsSold(0);

        when(productRepository.findById(1L)).thenReturn(Optional.of(product));

        // Act & Assert
        assertThrows(IllegalStateException.class, () -> orderService.createOrder(inputOrderDTO));
        verify(productRepository).findById(1L);
        verify(orderRepository, never()).save(any(Order.class));
    }

    @Test
    void createOrder_ProductNotFound_ThrowsResourceNotFoundException() {
        // Arrange
        OrderItemDTO orderItemDTO = OrderItemDTO.builder()
                .productId(1L)
                .quantity(2)
                .build();

        OrderDTO inputOrderDTO = OrderDTO.builder()
                .orderDate(LocalDateTime.now())
                .orderItems(List.of(orderItemDTO))
                .build();

        when(productRepository.findById(1L)).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(ResourceNotFoundException.class, () -> orderService.createOrder(inputOrderDTO));
        verify(productRepository).findById(1L);
        verify(orderRepository, never()).save(any(Order.class));
    }

    @Test
    void getAllOrders_OrdersExist_ReturnsListOfOrderDTOs() {
        // Arrange
        Order order1 = new Order();
        order1.setId(1L);
        order1.setOrderDate(LocalDateTime.now());
        Product product1 = new Product();
        product1.setId(1L);
        OrderItem orderItem1 = new OrderItem();
        orderItem1.setId(1L);
        orderItem1.setProduct(product1);
        orderItem1.setQuantity(2);
        orderItem1.setOrder(order1);
        order1.setOrderItems(List.of(orderItem1));

        Order order2 = new Order();
        order2.setId(2L);
        order2.setOrderDate(LocalDateTime.now().plusDays(1));
        Product product2 = new Product();
        product2.setId(2L);
        OrderItem orderItem2 = new OrderItem();
        orderItem2.setId(2L);
        orderItem2.setProduct(product2);
        orderItem2.setQuantity(1);
        orderItem2.setOrder(order2);
        order2.setOrderItems(List.of(orderItem2));

        when(orderRepository.findAll()).thenReturn(Arrays.asList(order1, order2));

        // Act
        List<OrderDTO> result = orderService.getAllOrders();

        // Assert
        assertEquals(2, result.size());
        assertEquals(1L, result.get(0).getId());
        assertEquals(2L, result.get(1).getId());
        assertEquals(1, result.get(0).getOrderItems().size());
        assertEquals(1, result.get(1).getOrderItems().size());
        assertEquals(1L, result.get(0).getOrderItems().get(0).getProductId());
        assertEquals(2L, result.get(1).getOrderItems().get(0).getProductId());

        verify(orderRepository).findAll();
    }

    @Test
    void getAllOrders_NoOrders_ReturnsEmptyList() {
        // Arrange
        when(orderRepository.findAll()).thenReturn(List.of());

        // Act
        List<OrderDTO> result = orderService.getAllOrders();

        // Assert
        assertTrue(result.isEmpty());
        verify(orderRepository).findAll();
    }
}
