package com.example.onlinestore.product;

import java.util.List;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

@Mapper(componentModel = "spring")
public interface ProductMapper {

    ProductMapper MAPPER = Mappers.getMapper(ProductMapper.class);

    @Mapping(target = "productId", source = "id")
    ProductDTO productToDto(Product product);

    // @Mapping(target = "productId", source = "id")
    // List<ProductDTO> productListToDto(List<Product> products);

}
