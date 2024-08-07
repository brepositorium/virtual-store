package com.acme.virtualstore.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class OrderItemDTO {

    private Long id;
    private Long productId;
    private Integer quantity;

}
