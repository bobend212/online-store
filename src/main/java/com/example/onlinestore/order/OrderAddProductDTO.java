package com.example.onlinestore.order;

import jakarta.validation.constraints.Min;
import lombok.*;

@Getter
@Setter
@Builder
public class OrderAddProductDTO {
    private Long orderId;
    private Long productId;

    @Min(1)
    private int qty;
}
