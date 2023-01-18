package com.example.onlinestore.product;

import java.math.BigDecimal;

import lombok.*;

@Getter
@Setter
@Builder
public class ProductForOrderDTO {
    private Long id;

    private String name;

    private BigDecimal price;
}
