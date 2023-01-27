package com.example.onlinestore.product;

import lombok.*;

import java.math.BigDecimal;

@Getter
@Setter
@Builder
public class ProductDTO {

    private Long id;

    private String name;

    private BigDecimal price;

    private Integer stockQty;

}
