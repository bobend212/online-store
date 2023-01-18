package com.example.onlinestore.orderItem;

import com.example.onlinestore.product.ProductForOrderDTO;

import lombok.*;

@Getter
@Setter
@Builder
public class OrderItemDTO {
    private ProductForOrderDTO product;
    private Integer qty;
}
