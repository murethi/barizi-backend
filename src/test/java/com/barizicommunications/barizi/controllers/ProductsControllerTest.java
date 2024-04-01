package com.barizicommunications.barizi.controllers;

import com.barizicommunications.barizi.dto.request.ProductRequest;
import com.barizicommunications.barizi.dto.response.ProductResponse;
import com.barizicommunications.barizi.models.Product;
import com.barizicommunications.barizi.service.ProductService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.util.List;
import java.util.UUID;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.mockito.Mockito.*;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(SpringExtension.class)
@SpringBootTest
@AutoConfigureMockMvc
class ProductsControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private ProductService productService;
    private static ObjectMapper objectMapper;

    @Autowired
    ProductsControllerTest(MockMvc mockMvc) {
        this.mockMvc = mockMvc;
    }

    @BeforeEach
    void beforeEach() {
        objectMapper = new ObjectMapper();
    }

    @Test
    void list_products_throw_401_if_not_logged_in() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get("/products"))

                .andExpect(status().isUnauthorized());
    }

    @WithMockUser(value = "barizi")
    @Test
    void index_will_return_list_of_products() throws Exception {
        ProductResponse productResponse = ProductResponse.builder()
                .id(UUID.randomUUID())
                .name("Java Coffee 500g")
                .description("Best coffee in Kenya")
                .minimumStockLevel(15)
                .build();
        List<ProductResponse> expected = List.of(productResponse);
        when(productService.findAll()).thenReturn(expected);
        mockMvc.perform(MockMvcRequestBuilders.get("/products"))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$", Matchers.hasSize(1)));

        verify(productService, times(1)).findAll();


    }

    @Test
    void create_will_throw_403_if_not_logged_in() throws Exception {
        ProductRequest productRequest = ProductRequest.builder()
                .name("Java Coffee 500g")
                .description("Best coffee in Kenya")
                .minimumStockLevel(15)
                .build();
        mockMvc.perform(MockMvcRequestBuilders.post("/products")
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(productRequest))
                )


                .andExpect(status().isUnauthorized());
    }

    @Test
    @WithMockUser(value = "barizi")
    void create_will_succeed() throws Exception {
        ProductRequest productRequest = ProductRequest.builder()
                .name("Java Coffee 500g")
                .description("Best coffee in Kenya")
                .minimumStockLevel(15)
                .build();
        doNothing().when(productService).create(any(ProductRequest.class));
        mockMvc.perform(MockMvcRequestBuilders.post("/products")
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(productRequest))
                )

                .andExpect(status().isCreated());
    }

    @Test
    void update_will_throw_403_if_not_logged_in() throws Exception {
        UUID productId = UUID.randomUUID();
        ProductRequest productRequest = ProductRequest.builder()
                .name("Java Coffee 500g")
                .description("Best coffee in Kenya")
                .minimumStockLevel(15)
                .build();
        mockMvc.perform(MockMvcRequestBuilders.put("/products/{productId}", productId)
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(productRequest))
                )


                .andExpect(status().isUnauthorized());
    }

    @Test
    @WithMockUser(value = "barizi")
    void will_update_product() throws Exception {
        UUID productId = UUID.randomUUID();
        ProductRequest productRequest = ProductRequest.builder()
                .name("Java Coffee 500g")
                .description("Best coffee in Kenya")
                .minimumStockLevel(15)
                .build();
        doNothing().when(productService).update(eq(productRequest), eq(productId));
        mockMvc.perform(MockMvcRequestBuilders.put("/products/{productId}", productId)
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(productRequest))
                )
                .andExpect(status().isAccepted());
    }

    @Test
    @WithMockUser(value = "barizi")
    void view_will_return_product_with_id() throws Exception {
        UUID productId = UUID.randomUUID();
        ProductResponse productResponse = ProductResponse.builder()
                .id(productId)
                .name("Java Coffee 500g")
                .description("Best coffee in Kenya")
                .minimumStockLevel(1500)
                .currentStock(2000)
                .build();
        when(productService.findOne(eq(productId))).thenReturn(productResponse);
        String response = mockMvc.perform(MockMvcRequestBuilders.get("/products/{productId}", productId)
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                )
                .andExpect(status().isOk()).andReturn().getResponse().getContentAsString();

        assertThat(response).isEqualTo(response);
        verify(productService, times(1)).findOne(any());
    }

    @Test
    void view_return_401_if_not_logged_in() throws Exception {
        UUID productId = UUID.randomUUID();
        mockMvc.perform(MockMvcRequestBuilders.get("/products/{productId}", productId)
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                )
                .andExpect(status().isUnauthorized());

        verify(productService, never()).findOne(any());
    }
}