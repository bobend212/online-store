package com.example.onlinestore.product;

import lombok.*;

import java.math.BigDecimal;

@Getter
@Setter
@Builder
public class ProductDTO {

    private Long productId;

    private String name;

    private BigDecimal price;

    private Integer stockQty;

    private Boolean inStock;

    // public static ProductDTO toDto(Product product) {
    // return ProductDTO.builder()
    // .id(product.getId())
    // .name(product.getName())
    // .price(product.getPrice())
    // .stockQty(product.getStockQty())
    // .inStock(product.getInStock())
    // .build();
    // }
}
