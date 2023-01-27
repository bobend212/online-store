package com.example.onlinestore.order;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

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

    @GetMapping("/{orderId}")
    public ResponseEntity<OrderDTO> getSingleOrder(@PathVariable Long orderId) {
        return new ResponseEntity<>(orderService.getSingleOrder(orderId), HttpStatus.OK);
    }

    @PutMapping("/{orderId}/clear")
    public ResponseEntity<OrderDTO> clearOrder(@PathVariable Long orderId) {
        return new ResponseEntity<>(orderService.clearOrder(orderId), HttpStatus.OK);
    }

    @DeleteMapping("/{orderId}")
    public ResponseEntity<Boolean> deleteOrder(@PathVariable Long orderId) {
        return new ResponseEntity<>(orderService.deleteOrder(orderId), HttpStatus.OK);
    }

    @PostMapping("/new-order")
    public ResponseEntity<OrderDTO> createNewOrder() {
        return new ResponseEntity<>(orderService.createNewOrder(), HttpStatus.CREATED);
    }

    @PostMapping("/add-product")
    public ResponseEntity<OrderDTO> addProductToOrder(@Valid @RequestBody OrderAddProductDTO requestBody) {
        return new ResponseEntity<>(orderService.addProductToOrder(requestBody), HttpStatus.CREATED);
    }

    @DeleteMapping("/{orderId}/delete-product/{productId}")
    public ResponseEntity<OrderDTO> deleteProductFromOrder(@PathVariable Long orderId, @PathVariable Long productId) {
        return new ResponseEntity<>(orderService.removeProductFromOrder(orderId, productId), HttpStatus.OK);
    }

    @PutMapping("/change-product-qty")
    public ResponseEntity<OrderDTO> changeProductQtyInTheOrder(@Valid @RequestBody OrderAddProductDTO requestBody) {
        return new ResponseEntity<>(orderService.changeProductQtyInOrder(requestBody), HttpStatus.OK);
    }

}
