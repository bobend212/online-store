package com.example.onlinestore.order;

import java.math.BigDecimal;
import java.text.MessageFormat;
import java.util.List;
import java.util.Optional;

import com.example.onlinestore.exception.NotFoundException;
import com.example.onlinestore.exception.ProductInTheOrderException;
import com.example.onlinestore.exception.ProductNotAvailableException;
import com.example.onlinestore.orderItem.OrderItem;
import com.example.onlinestore.orderItem.OrderItemRepository;
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

    @Transactional
    public OrderDTO addProductToOrder(OrderAddProductDTO requestBody) {

        var findOrder = orderRepository.findById(requestBody.getOrderId())
                .orElseThrow(() -> new NotFoundException(
                        MessageFormat.format("Order with ID: {0} not found.", requestBody.getOrderId())));

        var findProduct = productRepository.findById(requestBody.getProductId())
                .orElseThrow(() -> new NotFoundException(
                        MessageFormat.format("Product with ID: {0} not found.", requestBody.getProductId())));

        if (!checkIfProductIsAvailable(requestBody)) {
            throw new ProductNotAvailableException(
                    MessageFormat.format("Product with ID: {0} is not available.",
                            requestBody.getProductId()));
        }

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
        findProduct.setInStock(findProduct.getStockQty() <= 0 ? false : true);
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

        findOrder.setOrderStatus(OrderStatus.PROCESSING);
        findOrder.setOrderItems(orderItemsToReturn);
        orderRepository.save(findOrder);

        orderItemRepository.delete(findOrderItem);

        findProduct.setStockQty((findProduct.getStockQty() + findOrderItem.getQty()));
        findProduct.setInStock(findProduct.getStockQty() <= 0 ? false : true);
        productRepository.save(findProduct);

        return orderMapper.orderToDto(findOrder).withTotalPrice(updateTotalPrice(findOrder));
    }

    // change product qty in the order METHOD

    private Boolean checkIfProductIsAlreadyInTheOrder(OrderAddProductDTO requestBody) {
        return orderRepository.findById(requestBody.getOrderId()).get().getOrderItems().stream()
                .filter(product -> product.getId().equals(requestBody.getProductId()))
                .findFirst().isPresent();
    }

    private Boolean checkProductStockQty(OrderAddProductDTO requestBody) {
        return productRepository.findById(requestBody.getProductId()).map(product -> {
            return product.getStockQty() >= requestBody.getQty() ? true : false;
        }).orElse(false);
    }

    private Boolean checkIfProductIsAvailable(OrderAddProductDTO requestBody) {
        return productRepository.findById(requestBody.getProductId()).map(product -> {
            return product.getInStock();
        }).orElse(false);
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
