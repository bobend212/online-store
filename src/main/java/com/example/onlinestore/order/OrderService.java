package com.example.onlinestore.order;

import java.math.BigDecimal;
import java.text.MessageFormat;
import java.util.List;

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
            BigDecimal totalPrice = calculateTotalPrice(order.getOrderItems());
            return orderMapper.orderToDto(order).withTotalPrice(totalPrice);
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
                    MessageFormat.format("Product with ID: {0} is not available.", requestBody.getProductId()));
        }

        if (checkIfProductIsAlreadyInTheOrder(findOrder, findProduct)) {
            throw new ProductInTheOrderException(
                    MessageFormat.format("Product ID: {0} ({1}) is in the order already.",
                            requestBody.getProductId(), findProduct.getName()));
        }

        if (!checkProductStockQty(requestBody)) {
            throw new ProductNotAvailableException(
                    MessageFormat.format("Not enough stock quantity for Product ID: {0}. Current stock is {1}.",
                            requestBody.getProductId(), findProduct.getStockQty()));
        }

        orderItemRepository.save(OrderItem.builder()
                .order(findOrder)
                .product(findProduct)
                .qty(requestBody.getQty())
                .build());

        findOrder.setOrderStatus(OrderStatus.PROCESSING);
        orderRepository.save(findOrder);

        findProduct.setStockQty((findProduct.getStockQty() - requestBody.getQty()));
        findProduct.setInStock(findProduct.getStockQty() <= 0 ? false : true);
        productRepository.save(findProduct);

        BigDecimal totalPrice = calculateTotalPrice(findOrder.getOrderItems());
        return orderMapper.orderToDto(findOrder).withTotalPrice(totalPrice);
    }

    // change product qty in the order METHOD

    private Boolean checkIfProductIsAlreadyInTheOrder(Order findOrder, Product findProduct) {
        return findOrder.getOrderItems().stream().filter(product -> product.getProduct().equals(findProduct))
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

}
