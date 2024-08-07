package com.acme.virtualstore.service;

import com.acme.virtualstore.dto.OrderDTO;

import java.util.List;

public interface OrderService {

    OrderDTO createOrder(OrderDTO orderDTO);
    List<OrderDTO> getAllOrders();

}
