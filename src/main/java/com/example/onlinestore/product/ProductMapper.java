package com.example.onlinestore.product;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

@Mapper(componentModel = "spring")
public interface ProductMapper {

    ProductMapper MAPPER = Mappers.getMapper(ProductMapper.class);

    @Mapping(target = "productId", source = "id")
    ProductDTO productToDto(Product product);

    @Mapping(target = "id", ignore = true)
    Product createRequestDtoToProduct(ProductCreateDTO product);

}
