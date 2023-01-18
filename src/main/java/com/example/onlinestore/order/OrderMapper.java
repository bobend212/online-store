package com.example.onlinestore.order;

import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface OrderMapper {

    OrderDTO orderToDto(Order order) ;

}
