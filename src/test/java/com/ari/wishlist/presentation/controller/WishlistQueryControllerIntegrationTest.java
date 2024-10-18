package com.ari.wishlist.presentation.controller;

import com.ari.wishlist.domain.model.Wishlist;
import com.ari.wishlist.shared.config.IntegrationTestConfig;
import com.ari.wishlist.shared.data.IntegrationTestData;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;

import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.httpBasic;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

class WishlistQueryControllerIntegrationTest extends IntegrationTestConfig {

    @Test
    void whenGetAllProductsFromWishlist_thenShouldReturnProducts() throws Exception {
        mockMvc.perform(post(IntegrationTestData.BASE_URL + "/" + IntegrationTestData.CUSTOMER_ID + "/add-product/" + IntegrationTestData.PRODUCT_IDS[0])
                        .with(httpBasic(IntegrationTestData.ADMIN_USERNAME, IntegrationTestData.ADMIN_PASSWORD)))
                .andExpect(status().isOk());

        mockMvc.perform(post(IntegrationTestData.BASE_URL + "/" + IntegrationTestData.CUSTOMER_ID + "/add-product/" + IntegrationTestData.PRODUCT_IDS[1])
                        .with(httpBasic(IntegrationTestData.ADMIN_USERNAME, IntegrationTestData.ADMIN_PASSWORD)))
                .andExpect(status().isOk());

        mockMvc.perform(get(IntegrationTestData.BASE_URL + "/" + IntegrationTestData.CUSTOMER_ID)
                        .with(httpBasic(IntegrationTestData.ADMIN_USERNAME, IntegrationTestData.ADMIN_PASSWORD)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message").value(IntegrationTestData.PRODUCTS_RETRIEVED_SUCCESSFULLY))
                .andExpect(jsonPath("$.data[0].id").value(IntegrationTestData.PRODUCT_IDS[0]))
                .andExpect(jsonPath("$.data[1].id").value(IntegrationTestData.PRODUCT_IDS[1]));
    }

    @Test
    void whenGetAllProductsFromEmptyWishlist_thenShouldReturnEmptyWishlistException() throws Exception {
        wishlistRepository.save(Wishlist.builder()
                .customerId(IntegrationTestData.CUSTOMER_ID)
                .products(new ArrayList<>())
                .build());

        mockMvc.perform(get(IntegrationTestData.BASE_URL + "/" + IntegrationTestData.CUSTOMER_ID)
                        .with(httpBasic(IntegrationTestData.ADMIN_USERNAME, IntegrationTestData.ADMIN_PASSWORD)))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.message").value(IntegrationTestData.WISHLIST_IS_EMPTY_FOR_CUSTOMER + IntegrationTestData.CUSTOMER_ID));
    }

    @Test
    void whenCheckIfProductInWishlist_thenShouldReturnTrue() throws Exception {
        mockMvc.perform(post(IntegrationTestData.BASE_URL + "/" + IntegrationTestData.CUSTOMER_ID + "/add-product/" + IntegrationTestData.PRODUCT_IDS[0])
                        .with(httpBasic(IntegrationTestData.ADMIN_USERNAME, IntegrationTestData.ADMIN_PASSWORD)))
                .andExpect(status().isOk());

        mockMvc.perform(get(IntegrationTestData.BASE_URL + "/" + IntegrationTestData.CUSTOMER_ID + "/has-product/" + IntegrationTestData.PRODUCT_IDS[0])
                        .with(httpBasic(IntegrationTestData.ADMIN_USERNAME, IntegrationTestData.ADMIN_PASSWORD)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message").value(IntegrationTestData.PRODUCT_IN_WISHLIST))
                .andExpect(jsonPath("$.data").value(true));
    }

    @Test
    void whenCheckIfProductNotInWishlist_thenShouldReturn404() throws Exception {
        mockMvc.perform(get(IntegrationTestData.BASE_URL + "/" + IntegrationTestData.CUSTOMER_ID + "/has-product/" + IntegrationTestData.PRODUCT_IDS[0])
                        .with(httpBasic(IntegrationTestData.ADMIN_USERNAME, IntegrationTestData.ADMIN_PASSWORD)))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.message").value(IntegrationTestData.WISHLIST_NOT_FOUND_FOR_CUSTOMER + IntegrationTestData.CUSTOMER_ID));
    }

    @Test
    void whenCheckIfProductNotInWishlist_thenShouldReturnFalseAndMessage() throws Exception {
        mockMvc.perform(post(IntegrationTestData.BASE_URL + "/" + IntegrationTestData.CUSTOMER_ID + "/add-product/" + IntegrationTestData.PRODUCT_IDS[0])
                        .with(httpBasic(IntegrationTestData.ADMIN_USERNAME, IntegrationTestData.ADMIN_PASSWORD)))
                .andExpect(status().isOk());

        mockMvc.perform(get(IntegrationTestData.BASE_URL + "/" + IntegrationTestData.CUSTOMER_ID + "/has-product/product-123")
                        .with(httpBasic(IntegrationTestData.ADMIN_USERNAME, IntegrationTestData.ADMIN_PASSWORD)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message").value(IntegrationTestData.PRODUCT_NOT_IN_WISHLIST))
                .andExpect(jsonPath("$.data").value(false));
    }

    @Test
    void whenDeleteNonExistentWishlist_thenShouldReturnNotFound() throws Exception {
        mockMvc.perform(delete(IntegrationTestData.BASE_URL + "/" + IntegrationTestData.CUSTOMER_ID)
                        .with(httpBasic(IntegrationTestData.ADMIN_USERNAME, IntegrationTestData.ADMIN_PASSWORD)))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.message").value(IntegrationTestData.WISHLIST_NOT_FOUND_FOR_CUSTOMER + IntegrationTestData.CUSTOMER_ID));
    }
}
