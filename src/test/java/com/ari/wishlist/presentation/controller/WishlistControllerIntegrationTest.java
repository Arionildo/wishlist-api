package com.ari.wishlist.presentation.controller;

import com.ari.wishlist.domain.model.Product;
import com.ari.wishlist.domain.model.Wishlist;
import com.ari.wishlist.domain.repository.WishlistRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.web.servlet.MockMvc;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.httpBasic;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@TestPropertySource(locations = "classpath:application-test.properties")
class WishlistControllerIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private WishlistRepository wishlistRepository;

    private static final String BASE_URL = "/api/v1/wishlist";
    private static final String CUSTOMER_ID = "customer-1";
    private static final String ADMIN_USERNAME = "admin";
    private static final String ADMIN_PASSWORD = "admin";
    private static final BigDecimal PRODUCT_PRICE = BigDecimal.valueOf(102.0);
    private static final String PRODUCT_NAME = "Product 1";

    private static final int MAX_PRODUCTS = 2;
    private static final String[] PRODUCT_IDS = {"product-1", "product-2"};

    private static final String PRODUCT_ADDED_SUCCESSFULLY = "Product added successfully";
    private static final String PRODUCT_ALREADY_IN_WISHLIST = "Product is already in the wishlist";
    private static final String WISHLIST_LIMIT_EXCEEDED = "Wishlist cannot contain more than %d products";
    private static final String PRODUCT_REMOVED_SUCCESSFULLY = "Product removed successfully";
    private static final String WISHLIST_NOT_FOUND = "Wishlist not found";
    private static final String PRODUCTS_RETRIEVED_SUCCESSFULLY = "Products retrieved successfully";
    private static final String PRODUCT_IN_WISHLIST = "Product is in the customer's wishlist";
    private static final String PRODUCT_NOT_IN_WISHLIST = "Product is not in the customer's wishlist";
    private static final String WISHLIST_NOT_FOUND_FOR_CUSTOMER = "Wishlist not found for customer ID: ";
    private static final String WISHLIST_IS_EMPTY_FOR_CUSTOMER = "Wishlist is empty for customer ID: ";

    @BeforeEach
    void setUp() {
        wishlistRepository.deleteAll();
    }

    @AfterEach
    void tearDown() {
        wishlistRepository.deleteAll();
    }

    @Test
    void whenAddProductToWishlist_thenShouldPersistAndReturnWishlist() throws Exception {
        mockMvc.perform(post(BASE_URL + "/" + CUSTOMER_ID + "/add-product/" + PRODUCT_IDS[0])
                        .with(httpBasic(ADMIN_USERNAME, ADMIN_PASSWORD)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message").value(PRODUCT_ADDED_SUCCESSFULLY))
                .andExpect(jsonPath("$.data.customerId").value(CUSTOMER_ID))
                .andExpect(jsonPath("$.data.products[0].id").value(PRODUCT_IDS[0]))
                .andExpect(jsonPath("$.data.products[0].name").value(PRODUCT_NAME))
                .andExpect(jsonPath("$.data.products[0].price").value(PRODUCT_PRICE));

        List<Wishlist> wishlists = wishlistRepository.findAll();
        assertFalse(wishlists.isEmpty());

        Wishlist persistedWishlist = wishlists.get(0);
        assertEquals(CUSTOMER_ID, persistedWishlist.getCustomerId());

        List<Product> products = persistedWishlist.getProducts();
        assertFalse(products.isEmpty());
        assertEquals(PRODUCT_IDS[0], products.get(0).getProductId());
        assertEquals(PRODUCT_NAME, products.get(0).getName());
        assertEquals(0, products.get(0).getPrice().compareTo(PRODUCT_PRICE));
    }

    @Test
    void whenAddExistingProductToWishlist_thenShouldReturnConflict() throws Exception {
        mockMvc.perform(post(BASE_URL + "/" + CUSTOMER_ID + "/add-product/" + PRODUCT_IDS[0])
                        .with(httpBasic(ADMIN_USERNAME, ADMIN_PASSWORD)))
                .andExpect(status().isOk());

        mockMvc.perform(post(BASE_URL + "/" + CUSTOMER_ID + "/add-product/" + PRODUCT_IDS[0])
                        .with(httpBasic(ADMIN_USERNAME, ADMIN_PASSWORD)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message").value(PRODUCT_ALREADY_IN_WISHLIST));
    }

    @Test
    void whenAddProductExceedingMaxLimit_thenShouldReturnLimitExceeded() throws Exception {
        for (String productId : PRODUCT_IDS) {
            mockMvc.perform(post(BASE_URL + "/" + CUSTOMER_ID + "/add-product/" + productId)
                            .with(httpBasic(ADMIN_USERNAME, ADMIN_PASSWORD)))
                    .andExpect(status().isOk());
        }

        mockMvc.perform(post(BASE_URL + "/" + CUSTOMER_ID + "/add-product/product-3")
                        .with(httpBasic(ADMIN_USERNAME, ADMIN_PASSWORD)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message").value(String.format(WISHLIST_LIMIT_EXCEEDED, MAX_PRODUCTS)));
    }

    @Test
    void whenRemoveProductFromWishlist_thenShouldRemoveAndReturnUpdatedWishlist() throws Exception {
        mockMvc.perform(post(BASE_URL + "/" + CUSTOMER_ID + "/add-product/" + PRODUCT_IDS[0])
                        .with(httpBasic(ADMIN_USERNAME, ADMIN_PASSWORD)))
                .andExpect(status().isOk());

        mockMvc.perform(delete(BASE_URL + "/" + CUSTOMER_ID + "/remove-product/" + PRODUCT_IDS[0])
                        .with(httpBasic(ADMIN_USERNAME, ADMIN_PASSWORD)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message").value(PRODUCT_REMOVED_SUCCESSFULLY));

        List<Wishlist> wishlists = wishlistRepository.findAll();
        Wishlist wishlist = wishlists.get(0);
        assertFalse(wishlist.getProducts().stream()
                .anyMatch(product -> product.getProductId().equals(PRODUCT_IDS[0])));
    }

    @Test
    void whenRemoveProductFromEmptyWishlist_thenShouldReturnNotFound() throws Exception {
        mockMvc.perform(delete(BASE_URL + "/" + CUSTOMER_ID + "/remove-product/" + PRODUCT_IDS[0])
                        .with(httpBasic(ADMIN_USERNAME, ADMIN_PASSWORD)))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.message").value(WISHLIST_NOT_FOUND));
    }

    @Test
    void whenGetAllProductsFromWishlist_thenShouldReturnProducts() throws Exception {
        mockMvc.perform(post(BASE_URL + "/" + CUSTOMER_ID + "/add-product/" + PRODUCT_IDS[0])
                        .with(httpBasic(ADMIN_USERNAME, ADMIN_PASSWORD)))
                .andExpect(status().isOk());

        mockMvc.perform(post(BASE_URL + "/" + CUSTOMER_ID + "/add-product/" + PRODUCT_IDS[1])
                        .with(httpBasic(ADMIN_USERNAME, ADMIN_PASSWORD)))
                .andExpect(status().isOk());

        mockMvc.perform(get(BASE_URL + "/" + CUSTOMER_ID)
                        .with(httpBasic(ADMIN_USERNAME, ADMIN_PASSWORD)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message").value(PRODUCTS_RETRIEVED_SUCCESSFULLY))
                .andExpect(jsonPath("$.data[0].id").value(PRODUCT_IDS[0]))
                .andExpect(jsonPath("$.data[1].id").value(PRODUCT_IDS[1]));
    }

    @Test
    void whenGetAllProductsFromEmptyWishlist_thenShouldReturnEmptyWishlistException() throws Exception {
        wishlistRepository.save(Wishlist.builder()
                .customerId(CUSTOMER_ID)
                .products(new ArrayList<>())
                .build());

        mockMvc.perform(get(BASE_URL + "/" + CUSTOMER_ID)
                        .with(httpBasic(ADMIN_USERNAME, ADMIN_PASSWORD)))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.message").value(WISHLIST_IS_EMPTY_FOR_CUSTOMER + CUSTOMER_ID));
    }

    @Test
    void whenCheckIfProductInWishlist_thenShouldReturnTrue() throws Exception {
        mockMvc.perform(post(BASE_URL + "/" + CUSTOMER_ID + "/add-product/" + PRODUCT_IDS[0])
                        .with(httpBasic(ADMIN_USERNAME, ADMIN_PASSWORD)))
                .andExpect(status().isOk());

        mockMvc.perform(get(BASE_URL + "/" + CUSTOMER_ID + "/has-product/" + PRODUCT_IDS[0])
                        .with(httpBasic(ADMIN_USERNAME, ADMIN_PASSWORD)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message").value(PRODUCT_IN_WISHLIST))
                .andExpect(jsonPath("$.data").value(true));
    }

    @Test
    void whenCheckIfProductNotInWishlist_thenShouldReturn404() throws Exception {
        mockMvc.perform(get(BASE_URL + "/" + CUSTOMER_ID + "/has-product/" + PRODUCT_IDS[0])
                        .with(httpBasic(ADMIN_USERNAME, ADMIN_PASSWORD)))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.message").value(WISHLIST_NOT_FOUND_FOR_CUSTOMER + CUSTOMER_ID));
    }

    @Test
    void whenCheckIfProductNotInWishlist_thenShouldReturnFalseAndMessage() throws Exception {
        mockMvc.perform(post(BASE_URL + "/" + CUSTOMER_ID + "/add-product/" + PRODUCT_IDS[0])
                        .with(httpBasic(ADMIN_USERNAME, ADMIN_PASSWORD)))
                .andExpect(status().isOk());

        mockMvc.perform(get(BASE_URL + "/" + CUSTOMER_ID + "/has-product/product-123")
                        .with(httpBasic(ADMIN_USERNAME, ADMIN_PASSWORD)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message").value(PRODUCT_NOT_IN_WISHLIST))
                .andExpect(jsonPath("$.data").value(false));
    }

    @Test
    void whenDeleteNonExistentWishlist_thenShouldReturnNotFound() throws Exception {
        mockMvc.perform(delete(BASE_URL + "/" + CUSTOMER_ID)
                        .with(httpBasic(ADMIN_USERNAME, ADMIN_PASSWORD)))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.message").value(WISHLIST_NOT_FOUND_FOR_CUSTOMER + CUSTOMER_ID));
    }

    @Test
    void whenDeleteExistingWishlist_thenShouldReturnNoContent() throws Exception {
        mockMvc.perform(post(BASE_URL + "/" + CUSTOMER_ID + "/add-product/" + PRODUCT_IDS[0])
                        .with(httpBasic(ADMIN_USERNAME, ADMIN_PASSWORD)))
                .andExpect(status().isOk());

        mockMvc.perform(delete(BASE_URL + "/" + CUSTOMER_ID)
                        .with(httpBasic(ADMIN_USERNAME, ADMIN_PASSWORD)))
                .andExpect(status().isNoContent());
    }
}
