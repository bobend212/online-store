package com.example.onlinestore.order;

import java.math.BigDecimal;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

@Mapper(componentModel = "spring")
public interface OrderMapper {

    OrderMapper MAPPER = Mappers.getMapper(OrderMapper.class);

    @Mapping(target = "orderItemsFromDTO", source = "orderItems")
    @Mapping(target = "totalPrice", source = "order")
    OrderDTO orderToDto(Order order);

    default BigDecimal orderToTotal(Order order) {
        var x = order.getOrderItems();
        BigDecimal result = BigDecimal.ZERO;

        for (var product : x) {
            result = result.add(product.getProduct().getPrice().multiply(BigDecimal.valueOf(product.getQty())));
        }

        return result;
    }

}
