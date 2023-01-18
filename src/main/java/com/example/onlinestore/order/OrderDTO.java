package com.example.onlinestore.order;

import java.math.BigDecimal;
import java.util.List;

import com.example.onlinestore.orderItem.OrderItemDTO;

import lombok.*;

@Getter
@Setter
@Builder
public class OrderDTO {

    private Long id;

    private OrderStatus orderStatus;

    @With
    private BigDecimal totalPrice;

    private List<OrderItemDTO> orderItems;



}
