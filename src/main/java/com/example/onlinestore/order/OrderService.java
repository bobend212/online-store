package com.example.onlinestore.order;

import java.math.BigDecimal;
import java.util.List;

import com.example.onlinestore.orderItem.OrderItem;
import org.springframework.stereotype.Service;

@Service
public class OrderService {

    private final OrderRepository orderRepository;
    private final OrderMapper orderMapper;

    public OrderService(OrderRepository orderRepository, OrderMapper orderMapper) {
        this.orderRepository = orderRepository;
        this.orderMapper = orderMapper;
    }

    public List<OrderDTO> getAllOrders() {
        return orderRepository.findAll().stream().map(order -> {
            BigDecimal totalPrice = calculateTotalPrice(order.getOrderItems());
            return orderMapper.orderToDto(order).withTotalPrice(totalPrice);
        }).toList();
    }

    private BigDecimal calculateTotalPrice(List<OrderItem> orderItems) {
        return orderItems.stream()
                .map(product -> product.getProduct().getPrice()
                        .multiply(BigDecimal.valueOf(product.getQty())))
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }

}
