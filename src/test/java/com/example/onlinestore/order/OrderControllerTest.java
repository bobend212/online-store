package com.example.onlinestore.order;

import com.example.onlinestore.product.ProductService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
public class OrderControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ProductService productService;

    ObjectMapper objectMapper = new ObjectMapper();

    @Test
    void should_update_product_stock_quantity_after_order_delete() throws Exception {

        mockMvc.perform(delete("/api/v1/orders/1"))
                .andExpect(status().isOk())
                .andReturn();

        var result = productService.getSingleProductById(1L).getStockQty();

        Assertions.assertEquals(15, result);
    }

    @Test
    void new_order_should_be_empty() throws Exception {

        mockMvc.perform(post("/api/v1/orders"));

        MvcResult mvcResult = mockMvc.perform(get("/api/v1/orders/3"))
                .andExpect(status().isOk())
                .andReturn();

        String contentAsString = mvcResult.getResponse().getContentAsString();

        var order = objectMapper.readValue(contentAsString, OrderDTO.class);

        Assertions.assertEquals(OrderStatus.EMPTY, order.getOrderStatus());
    }

}
