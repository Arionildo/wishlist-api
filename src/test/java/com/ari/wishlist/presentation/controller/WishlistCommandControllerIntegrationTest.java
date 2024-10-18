package com.ari.wishlist.presentation.controller;

import com.ari.wishlist.domain.model.Wishlist;
import com.ari.wishlist.shared.config.IntegrationTestConfig;
import com.ari.wishlist.shared.data.IntegrationTestData;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.httpBasic;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

class WishlistCommandControllerIntegrationTest extends IntegrationTestConfig {

    @Test
    void whenAddProductToWishlist_thenShouldPersistAndReturnWishlist() throws Exception {
        mockMvc.perform(post(IntegrationTestData.BASE_URL + "/" + IntegrationTestData.CUSTOMER_ID + "/add-product/" + IntegrationTestData.PRODUCT_IDS[0])
                        .with(httpBasic(IntegrationTestData.ADMIN_USERNAME, IntegrationTestData.ADMIN_PASSWORD)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message").value(IntegrationTestData.PRODUCT_ADDED_SUCCESSFULLY));

        List<Wishlist> wishlists = wishlistRepository.findAll();
        assertFalse(wishlists.isEmpty());
    }

    @Test
    void whenAddExistingProductToWishlist_thenShouldReturnConflict() throws Exception {
        mockMvc.perform(post(IntegrationTestData.BASE_URL + "/" + IntegrationTestData.CUSTOMER_ID + "/add-product/" + IntegrationTestData.PRODUCT_IDS[0])
                        .with(httpBasic(IntegrationTestData.ADMIN_USERNAME, IntegrationTestData.ADMIN_PASSWORD)))
                .andExpect(status().isOk());

        mockMvc.perform(post(IntegrationTestData.BASE_URL + "/" + IntegrationTestData.CUSTOMER_ID + "/add-product/" + IntegrationTestData.PRODUCT_IDS[0])
                        .with(httpBasic(IntegrationTestData.ADMIN_USERNAME, IntegrationTestData.ADMIN_PASSWORD)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message").value(IntegrationTestData.PRODUCT_ALREADY_IN_WISHLIST));
    }

    @Test
    void whenAddProductExceedingMaxLimit_thenShouldReturnLimitExceeded() throws Exception {
        for (String productId : IntegrationTestData.PRODUCT_IDS) {
            mockMvc.perform(post(IntegrationTestData.BASE_URL + "/" + IntegrationTestData.CUSTOMER_ID + "/add-product/" + productId)
                            .with(httpBasic(IntegrationTestData.ADMIN_USERNAME, IntegrationTestData.ADMIN_PASSWORD)))
                    .andExpect(status().isOk());
        }

        mockMvc.perform(post(IntegrationTestData.BASE_URL + "/" + IntegrationTestData.CUSTOMER_ID + "/add-product/product-3")
                        .with(httpBasic(IntegrationTestData.ADMIN_USERNAME, IntegrationTestData.ADMIN_PASSWORD)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message").value(String.format(IntegrationTestData.WISHLIST_LIMIT_EXCEEDED, 2)));
    }

    @Test
    void whenRemoveProductFromWishlist_thenShouldRemoveAndReturnUpdatedWishlist() throws Exception {
        mockMvc.perform(post(IntegrationTestData.BASE_URL + "/" + IntegrationTestData.CUSTOMER_ID + "/add-product/" + IntegrationTestData.PRODUCT_IDS[0])
                        .with(httpBasic(IntegrationTestData.ADMIN_USERNAME, IntegrationTestData.ADMIN_PASSWORD)))
                .andExpect(status().isOk());

        mockMvc.perform(delete(IntegrationTestData.BASE_URL + "/" + IntegrationTestData.CUSTOMER_ID + "/remove-product/" + IntegrationTestData.PRODUCT_IDS[0])
                        .with(httpBasic(IntegrationTestData.ADMIN_USERNAME, IntegrationTestData.ADMIN_PASSWORD)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message").value(IntegrationTestData.PRODUCT_REMOVED_SUCCESSFULLY));

        List<Wishlist> wishlists = wishlistRepository.findAll();
        Wishlist wishlist = wishlists.get(0);
        assertFalse(wishlist.getProducts().stream()
                .anyMatch(product -> product.getProductId().equals(IntegrationTestData.PRODUCT_IDS[0])));
    }

    @Test
    void whenRemoveProductFromEmptyWishlist_thenShouldReturnNotFound() throws Exception {
        mockMvc.perform(delete(IntegrationTestData.BASE_URL + "/" + IntegrationTestData.CUSTOMER_ID + "/remove-product/" + IntegrationTestData.PRODUCT_IDS[0])
                        .with(httpBasic(IntegrationTestData.ADMIN_USERNAME, IntegrationTestData.ADMIN_PASSWORD)))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.message").value(IntegrationTestData.WISHLIST_NOT_FOUND));
    }

    @Test
    void whenDeleteExistingWishlist_thenShouldReturnNoContent() throws Exception {
        mockMvc.perform(post(IntegrationTestData.BASE_URL + "/" + IntegrationTestData.CUSTOMER_ID + "/add-product/" + IntegrationTestData.PRODUCT_IDS[0])
                        .with(httpBasic(IntegrationTestData.ADMIN_USERNAME, IntegrationTestData.ADMIN_PASSWORD)))
                .andExpect(status().isOk());

        mockMvc.perform(delete(IntegrationTestData.BASE_URL + "/" + IntegrationTestData.CUSTOMER_ID)
                        .with(httpBasic(IntegrationTestData.ADMIN_USERNAME, IntegrationTestData.ADMIN_PASSWORD)))
                .andExpect(status().isNoContent());
    }
}
