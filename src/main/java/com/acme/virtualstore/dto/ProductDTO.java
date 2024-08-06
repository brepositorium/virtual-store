package com.acme.virtualstore.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class ProductDTO {

    private Long id;
    private String name;
    private Integer itemsOnStock;
    private Integer itemsSold;

}
