package com.acme.virtualstore.service.impl;

import com.acme.virtualstore.dto.OrderDTO;
import com.acme.virtualstore.dto.OrderItemDTO;
import com.acme.virtualstore.entity.Order;
import com.acme.virtualstore.entity.OrderItem;
import com.acme.virtualstore.exception.ResourceNotFoundException;
import com.acme.virtualstore.repository.OrderRepository;
import com.acme.virtualstore.repository.ProductRepository;
import com.acme.virtualstore.service.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class OrderServiceImpl implements OrderService {

    private final OrderRepository orderRepository;
    private final ProductRepository productRepository;

    @Autowired
    public OrderServiceImpl(OrderRepository orderRepository, ProductRepository productRepository) {
        this.orderRepository = orderRepository;
        this.productRepository = productRepository;
    }

    @Override
    public OrderDTO createOrder(OrderDTO orderDTO) {
        Order order = convertToEntity(orderDTO);
        Order savedOrder = orderRepository.save(order);
        return convertOrderToDTO(savedOrder);
    }

    @Override
    public List<OrderDTO> getAllOrders() {
        List<Order> orders = orderRepository.findAll();
        return orders.stream().map(this::convertOrderToDTO).toList();
    }

    private OrderDTO convertOrderToDTO(Order order) {
        List<OrderItemDTO> orderItemDTOs = order.getOrderItems().stream()
                .map(this::convertOrderItemToDTO)
                .toList();

        return OrderDTO.builder()
                .id(order.getId())
                .orderItems(orderItemDTOs)
                .orderDate(order.getOrderDate())
                .build();
    }

    private OrderItemDTO convertOrderItemToDTO(OrderItem orderItem) {
        return OrderItemDTO.builder()
                .id(orderItem.getId())
                .orderId(orderItem.getOrder().getId())
                .productId(orderItem.getProduct().getId())
                .quantity(orderItem.getQuantity())
                .build();
    }

    private Order convertToEntity(OrderDTO orderDTO) {
        Order order = new Order();
        order.setId(orderDTO.getId());
        order.setOrderDate(orderDTO.getOrderDate());

        List<OrderItem> orderItems = orderDTO.getOrderItems().stream()
                .map(itemDTO -> convertToOrderItemEntity(itemDTO, order))
                .toList();

        order.setOrderItems(orderItems);
        return order;
    }

    private OrderItem convertToOrderItemEntity(OrderItemDTO orderItemDTO, Order order) {
        OrderItem orderItem = new OrderItem();
        orderItem.setId(orderItemDTO.getId());
        orderItem.setOrder(order);
        orderItem.setProduct(productRepository.findById(orderItemDTO.getProductId())
                .orElseThrow(() -> new ResourceNotFoundException("Product not found")));
        orderItem.setQuantity(orderItemDTO.getQuantity());
        return orderItem;
    }
}
