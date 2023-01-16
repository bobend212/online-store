package com.example.onlinestore.orderItem;

import com.example.onlinestore.order.Order;
import com.example.onlinestore.product.Product;
import jakarta.persistence.*;
import lombok.*;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Getter
@Setter
public class OrderItem {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name="order_id", nullable=false)
    private Order order;

    @OneToOne
    @JoinColumn(name="product_id", nullable=false)
    private Product product;

    private Integer qty;

}