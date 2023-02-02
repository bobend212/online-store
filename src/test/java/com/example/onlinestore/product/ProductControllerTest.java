package com.example.onlinestore.product;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import java.math.BigDecimal;
import java.util.Arrays;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
class ProductControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ProductService productService;

    ObjectMapper objectMapper = new ObjectMapper();

    @Test
    @Order(1)
    void should_update_stock_quantity_after_order_delete() throws Exception {

        mockMvc.perform(delete("/api/v1/orders/1"))
                .andExpect(status().isOk())
                .andReturn();

        var result = productService.getSingleProductById(1L).getStockQty();

        Assertions.assertEquals(15, result);
    }

    @Test
    @Order(2)
    void should_create_and_return_list_of_products() throws Exception {

        objectMapper.writeValueAsBytes(ProductCreateDTO.builder()
                .name("product1")
                .price(new BigDecimal("20"))
                .stockQty(100)
                .build());

        mockMvc.perform(post("/api/v1/products")
                        .content(objectMapper.writeValueAsBytes(ProductCreateDTO.builder()
                                .name("product1")
                                .price(new BigDecimal("20"))
                                .stockQty(100)
                                .build()))
                        .contentType("application/json"))
                .andExpect(status().isCreated());

        MvcResult mvcResult = mockMvc.perform(get("/api/v1/products"))
                .andExpect(status().isOk())
                .andReturn();

        var products = Arrays.asList(
                objectMapper.readValue(mvcResult.getResponse().getContentAsString(), Product[].class));

        Assertions.assertEquals(4, products.size());
    }


}