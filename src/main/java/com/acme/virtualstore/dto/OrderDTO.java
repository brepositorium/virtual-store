package com.acme.virtualstore.dto;

import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

@Data
@Builder
public class OrderDTO {

    private Long id;
    private List<OrderItemDTO> orderItems;
    private LocalDateTime orderDate;

}
