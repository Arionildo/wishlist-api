package com.ari.wishlist.application.mapper;

import com.ari.wishlist.application.dto.WishlistDTO;
import com.ari.wishlist.application.exception.WishlistMappingException;
import com.ari.wishlist.domain.model.Product;
import com.ari.wishlist.domain.model.Wishlist;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;

class WishlistMapperTest {

    @Test
    void givenValidWishlist_whenToDTO_thenReturnsWishlistDTO() {
        BigDecimal priceA = BigDecimal.valueOf(100.0);
        BigDecimal priceB = BigDecimal.valueOf(150.0);
        Product product1 = Product.builder()
                .productId("product-1")
                .name("Product 1")
                .price(priceA)
                .build();
        Product product2 = Product.builder()
                .productId("product-2")
                .name("Product 2")
                .price(priceB)
                .build();
        Wishlist wishlist = Wishlist.builder()
                .id("wishlist-1")
                .customerId("customer-1")
                .products(Arrays.asList(product1, product2))
                .build();

        WishlistDTO wishlistDTO = WishlistMapper.toDTO(wishlist);

        assertNotNull(wishlistDTO);
        assertEquals("wishlist-1", wishlistDTO.getId());
        assertEquals("customer-1", wishlistDTO.getCustomerId());
        assertEquals(2, wishlistDTO.getProducts().size());
        assertEquals("product-1", wishlistDTO.getProducts().get(0).getId());
        assertEquals("product-2", wishlistDTO.getProducts().get(1).getId());
    }

    @Test
    void givenNullWishlist_whenToDTO_thenThrowsException() {
        Exception exception = assertThrows(WishlistMappingException.class,
                () -> WishlistMapper.toDTO(null));

        assertEquals("Wishlist cannot be null", exception.getMessage());
    }

    @Test
    void givenWishlistWithNullProducts_whenToDTO_thenReturnsWishlistDTOWithEmptyProducts() {
        Wishlist wishlist = Wishlist.builder()
                .id("wishlist-2")
                .customerId("customer-2")
                .products(null)
                .build();

        WishlistDTO wishlistDTO = WishlistMapper.toDTO(wishlist);

        assertNotNull(wishlistDTO);
        assertEquals("wishlist-2", wishlistDTO.getId());
        assertEquals("customer-2", wishlistDTO.getCustomerId());
        assertTrue(wishlistDTO.getProducts().isEmpty());
    }

    @Test
    void givenWishlistWithEmptyProducts_whenToDTO_thenReturnsWishlistDTOWithEmptyProducts() {
        Wishlist wishlist = Wishlist.builder()
                .id("wishlist-3")
                .customerId("customer-3")
                .products(List.of())
                .build();

        WishlistDTO wishlistDTO = WishlistMapper.toDTO(wishlist);

        assertNotNull(wishlistDTO);
        assertEquals("wishlist-3", wishlistDTO.getId());
        assertEquals("customer-3", wishlistDTO.getCustomerId());
        assertTrue(wishlistDTO.getProducts().isEmpty());
    }
}
