package com.example.onlinestore.product;

import com.example.onlinestore.exception.NotFoundException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class ProductServiceTest {

    @InjectMocks
    private ProductService productService;

    @Mock
    private ProductRepository productRepository;

    @Mock
    private ProductMapper productMapper;

    @Test
    void should_throw_notFoundException_when_product_not_exist() {

        //given
        when(productRepository.findById(1L)).thenReturn(Optional.empty());

        //when
        //then
        Exception exception = assertThrows(NotFoundException.class, () -> {
            productService.getSingleProductById(1L);
        });

        String expectedMessage = "Product with ID: 1 not found.";
        String actualMessage = exception.getMessage();

        assertTrue(actualMessage.contains(expectedMessage));
    }

}
