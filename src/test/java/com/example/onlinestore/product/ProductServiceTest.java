package com.example.onlinestore.product;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.when;

import java.math.BigDecimal;
import java.util.List;

@ExtendWith(MockitoExtension.class)
public class ProductServiceTest {

    private ProductService productService;

    @Mock
    private ProductRepository productRepository;

    @Mock
    private ProductMapper productMapper;

    @BeforeEach
    void setUp() {
        productService = new ProductService(productRepository, productMapper);
    }

    @Test
    void should_find_all_products() {

        when(productRepository.findAll()).thenReturn(
                List.of(Product.builder().name("Sausage").price(new BigDecimal("5")).stockQty(100).build()));

        List<ProductDTO> actualService = productService.getAllProducts();
        List<Product> actualRepo = productRepository.findAll();

        assertTrue(actualService.size() > 0);
    }

    @Test
    void should_create_product() {

    }

    @Test
    void should_find_product_by_id() {

    }

    @Test
    void should_update_product_by_id() {

    }

    @Test
    void should_delete_product_by_id() {

    }

    @Test
    void should_throw_exception_when_product_not_exist() {

    }

}
