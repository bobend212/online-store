package com.example.onlinestore.order;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface OrderMapper {

    @Mapping(target = "totalPrice", ignore = true)
    OrderDTO orderToDto(Order order);

}
