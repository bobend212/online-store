package com.example.onlinestore.product;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.mockito.Mockito.verify;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

import static org.mockito.BDDMockito.given;

import java.math.BigDecimal;

@ExtendWith(MockitoExtension.class)
public class ProductServiceTest {

    @Mock
    private ProductRepository productRepository;

    private ProductService productService;

    @Mock
    ProductMapper productMapper;

    @BeforeEach
    void setUp() {
        productService = new ProductService(productRepository, productMapper);
    }

    @Test
    void canGetAllProducts() {
        // when
        productService.getAllProducts();
        // then
        verify(productRepository).findAll();
    }

    @Test
    void canCreateProduct() {
        // given
        ProductCreateDTO productDto = ProductCreateDTO.builder()
                .name("ProductName")
                .price(new BigDecimal("10"))
                .stockQty(100)
                .build();

        // when
        productService.addProduct(productDto);

        // then
        ArgumentCaptor<Product> productArgumentCaptor = ArgumentCaptor.forClass(Product.class);
        verify(productRepository).save(productArgumentCaptor.capture());
        Product capturedProduct = productArgumentCaptor.getValue();
        assertThat(capturedProduct.getName()).isEqualTo(productDto.getName());
    }

}
