package com.example.onlinestore.product;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
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
        ProductRepository productRepository;

        @BeforeEach
        void cleanUp() {
                productRepository.deleteAll();
        }

        ObjectMapper objectMapper = new ObjectMapper();

        @Test
        void shouldCreateOneProductAndGetListOfProducts() throws Exception {
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

                Assertions.assertEquals(1, products.size());
                Assertions.assertEquals("product1", products.get(0).getName());
        }

}