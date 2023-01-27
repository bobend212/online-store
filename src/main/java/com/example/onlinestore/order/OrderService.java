package com.example.onlinestore.order;

import java.math.BigDecimal;
import java.text.MessageFormat;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import com.example.onlinestore.exception.NotFoundException;
import com.example.onlinestore.exception.ProductInTheOrderException;
import com.example.onlinestore.exception.ProductNotAvailableException;
import com.example.onlinestore.orderItem.OrderItem;
import com.example.onlinestore.orderItem.OrderItemRepository;
import com.example.onlinestore.product.Product;
import com.example.onlinestore.product.ProductRepository;

import jakarta.transaction.Transactional;

import org.springframework.stereotype.Service;

@Service
public class OrderService {

    private final ProductRepository productRepository;
    private final OrderItemRepository orderItemRepository;
    private final OrderRepository orderRepository;
    private final OrderMapper orderMapper;

    public OrderService(OrderRepository orderRepository, OrderMapper orderMapper,
                        OrderItemRepository orderItemRepository, ProductRepository productRepository) {
        this.orderRepository = orderRepository;
        this.orderMapper = orderMapper;
        this.orderItemRepository = orderItemRepository;
        this.productRepository = productRepository;
    }

    public List<OrderDTO> getAllOrders() {
        return orderRepository.findAll().stream().map(order -> {
            return orderMapper.orderToDto(order).withTotalPrice(updateTotalPrice(order));
        }).toList();
    }

    public OrderDTO getSingleOrder(Long orderId) {
        return orderRepository.findById(orderId).map(order -> {
            return orderMapper.orderToDto(order).withTotalPrice(updateTotalPrice(order));
        }).orElseThrow(() -> new NotFoundException(
                MessageFormat.format("Order with ID: {0} not found.", orderId)));
    }

    public OrderDTO createNewOrder() {
        return orderMapper.orderToDto(orderRepository.save(Order.builder()
                .orderStatus(OrderStatus.EMPTY)
                .build()));
    }

    public OrderDTO clearOrder(Long orderId) {
        return orderRepository.findById(orderId).map(order -> {
            orderItemRepository.deleteAllInBatch(order.getOrderItems());
            order.setOrderStatus(OrderStatus.EMPTY);
            order.setOrderItems(Collections.emptyList());
            orderRepository.save(order);
            return orderMapper.orderToDto(order);
        }).orElseThrow(() -> new NotFoundException(
                MessageFormat.format("Order with ID: {0} not found.", orderId)));
    }

    @Transactional
    public OrderDTO addProductToOrder(OrderAddProductDTO requestBody) {

        var findOrder = orderRepository.findById(requestBody.getOrderId())
                .orElseThrow(() -> new NotFoundException(
                        MessageFormat.format("Order with ID: {0} not found.", requestBody.getOrderId())));

        var findProduct = productRepository.findById(requestBody.getProductId())
                .orElseThrow(() -> new NotFoundException(
                        MessageFormat.format("Product with ID: {0} not found.", requestBody.getProductId())));

        if (checkIfProductIsAlreadyInTheOrder(requestBody)) {
            throw new ProductInTheOrderException(
                    MessageFormat.format("Product ID: {0} ({1}) is in the order already.",
                            requestBody.getProductId(), findProduct.getName()));
        }

        if (!checkProductStockQty(requestBody)) {
            throw new ProductNotAvailableException(
                    MessageFormat.format("Not enough stock quantity for Product ID: {0}. Current stock is {1}.",
                            requestBody.getProductId(), findProduct.getStockQty()));
        }

        var newOrderItem = orderItemRepository.save(OrderItem.builder()
                .order(findOrder)
                .product(findProduct)
                .qty(requestBody.getQty())
                .build());

        var orderItemsToReturn = Optional.ofNullable(findOrder.getOrderItems()).map(e -> {
            e.add(newOrderItem);
            return e;
        }).orElse(List.of(newOrderItem));

        findOrder.setOrderStatus(OrderStatus.PROCESSING);
        findOrder.setOrderItems(orderItemsToReturn);
        orderRepository.save(findOrder);

        findProduct.setStockQty((findProduct.getStockQty() - requestBody.getQty()));
        productRepository.save(findProduct);

        return orderMapper.orderToDto(findOrder).withTotalPrice(updateTotalPrice(findOrder));
    }

    @Transactional
    public OrderDTO removeProductFromOrder(Long orderId, Long productId) {

        var findOrder = orderRepository.findById(orderId)
                .orElseThrow(() -> new NotFoundException(
                        MessageFormat.format("Order with ID: {0} not found.", orderId)));

        var findProduct = productRepository.findById(productId)
                .orElseThrow(() -> new NotFoundException(
                        MessageFormat.format("Product with ID: {0} not found.", productId)));

        var findOrderItem = orderRepository.findById(orderId).get().getOrderItems().stream()
                .filter(product -> product.getProduct().getId().equals(productId)).findFirst()
                .orElseThrow(() -> new NotFoundException(
                        MessageFormat.format("Product with ID: {0} is not associated with this order.", productId)));

        var orderItemsToReturn = Optional.ofNullable(findOrder.getOrderItems()).map(e -> {
            e.remove(findOrderItem);
            return e;
        }).orElse(List.of(findOrderItem));

        orderItemRepository.delete(findOrderItem);

        if (findOrder.getOrderItems().size() == 0) {
            findOrder.setOrderStatus(OrderStatus.EMPTY);
        } else {
            findOrder.setOrderStatus(OrderStatus.PROCESSING);
        }
        findOrder.setOrderItems(orderItemsToReturn);
        orderRepository.save(findOrder);

        findProduct.setStockQty((findProduct.getStockQty() + findOrderItem.getQty()));
        productRepository.save(findProduct);

        return orderMapper.orderToDto(findOrder).withTotalPrice(updateTotalPrice(findOrder));
    }

    @Transactional
    public OrderDTO changeProductQtyInOrder(OrderAddProductDTO requestBody) {
        var findOrder = orderRepository.findById(requestBody.getOrderId())
                .orElseThrow(() -> new NotFoundException(
                        MessageFormat.format("Order with ID: {0} not found.", requestBody.getOrderId())));

        var findProduct = productRepository.findById(requestBody.getProductId())
                .orElseThrow(() -> new NotFoundException(
                        MessageFormat.format("Product with ID: {0} not found.", requestBody.getProductId())));

        var findOrderItem = orderRepository.findById(requestBody.getOrderId()).get().getOrderItems().stream()
                .filter(product -> product.getProduct().getId().equals(requestBody.getProductId())).findFirst()
                .orElseThrow(() -> new NotFoundException(
                        MessageFormat.format("Product with ID: {0} is not associated with this order.", requestBody.getProductId())));

        if (findOrderItem.getQty() > requestBody.getQty()) {
            findProduct.setStockQty((findProduct.getStockQty() + findOrderItem.getQty() - requestBody.getQty()));
        } else {
            findProduct.setStockQty((findProduct.getStockQty() - Math.abs(findOrderItem.getQty() - requestBody.getQty())));
        }

        if (findProduct.getStockQty() < 0) {
            throw new ProductNotAvailableException(
                    MessageFormat.format("Not enough stock quantity for Product ID: {0}.",
                            requestBody.getProductId()));
        } else {
            productRepository.save(findProduct);
        }

        findOrderItem.setQty(requestBody.getQty());
        orderItemRepository.save(findOrderItem);

        return orderMapper.orderToDto(findOrder).withTotalPrice(updateTotalPrice(findOrder));
    }


    private Boolean checkIfProductIsAlreadyInTheOrder(OrderAddProductDTO requestBody) {
        return getSingleOrder(requestBody.getOrderId()).getOrderItems().stream()
                        .anyMatch(product -> product.getProduct().getId().equals((requestBody.getProductId())));
    }

    private Boolean checkProductStockQty(OrderAddProductDTO requestBody) {
        return productRepository.findById(requestBody.getProductId())
                .map(product -> product.getStockQty() >= requestBody.getQty()
        ).orElse(false);
    }

    private BigDecimal calculateTotalPrice(List<OrderItem> orderItems) {
        return orderItems.stream()
                .map(product -> product.getProduct().getPrice()
                        .multiply(BigDecimal.valueOf(product.getQty())))
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }

    private BigDecimal updateTotalPrice(Order findOrder) {
        return calculateTotalPrice(findOrder.getOrderItems());
    }

}
