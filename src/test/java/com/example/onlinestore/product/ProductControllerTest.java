package com.example.onlinestore.product;

import com.fasterxml.jackson.databind.ObjectMapper;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import java.math.BigDecimal;
import java.util.Arrays;

import static org.junit.jupiter.api.Assertions.assertEquals;
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
        void should_create_and_return_list_of_products() throws Exception {

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

        @Test
        void should_update_all_product_fields() throws Exception {

                var productToUpdate = ProductUpdateDTO.builder()
                                .name("Sausage")
                                .price(new BigDecimal("15"))
                                .stockQty(500)
                                .build();

                mockMvc.perform(put("/api/v1/products/1")
                                .contentType("application/json")
                                .content(objectMapper.writeValueAsBytes(productToUpdate)))
                                .andExpect(status().isOk());

                MvcResult mvcResult = mockMvc.perform(get("/api/v1/products/1"))
                                .andExpect(status().isOk())
                                .andReturn();

                var product = objectMapper.readValue(mvcResult.getResponse().getContentAsString(), Product.class);

                Assertions.assertAll(() -> {
                        assertEquals("Sausage", product.getName());
                        assertEquals(new BigDecimal("15.00"), product.getPrice());
                        assertEquals(500, product.getStockQty());
                });

        }

        @Test
        void should_delete_product() throws Exception {

                var productsCountBeforeDelete = productService.getAllProducts().size();

                mockMvc.perform(delete("/api/v1/products/3"))
                                .andExpect(status().isOk())
                                .andReturn();

                var productsCountAfterDelete = productService.getAllProducts().size();

                Assertions.assertEquals(productsCountBeforeDelete - 1, productsCountAfterDelete);
        }

}