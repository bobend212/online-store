package com.example.onlinestore.product;

import java.math.BigDecimal;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class ProductCreateDTO {
    private String name;

    private BigDecimal price;

    private Integer stockQty;

}
