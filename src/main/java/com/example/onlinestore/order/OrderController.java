package com.example.onlinestore.order;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/v1/orders")
public class OrderController {

    private final OrderService orderService;

    public OrderController(OrderService orderService) {
        this.orderService = orderService;
    }

    @GetMapping()
    public ResponseEntity<List<OrderDTO>> getAllOrders() {
        return new ResponseEntity<>(orderService.getAllOrders(), HttpStatus.OK);
    }

    @PostMapping("/add-product")
    public ResponseEntity<OrderDTO> addProductToOrder(@Valid @RequestBody OrderAddProductDTO requestBody) {
        return new ResponseEntity<>(orderService.addProductToOrder(requestBody), HttpStatus.CREATED);
    }

    @DeleteMapping("/{orderId}/delete-product/{productId}")
    public ResponseEntity<OrderDTO> deleteProductFromOrder(@PathVariable Long orderId, @PathVariable Long productId) {
        return new ResponseEntity<>(orderService.removeProductFromOrder(orderId, productId), HttpStatus.OK);
    }
}
