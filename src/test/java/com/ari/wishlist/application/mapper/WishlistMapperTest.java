package com.ari.wishlist.application.mapper;

import com.ari.wishlist.application.dto.WishlistDTO;
import com.ari.wishlist.application.exception.WishlistMappingException;
import com.ari.wishlist.domain.model.Product;
import com.ari.wishlist.domain.model.Wishlist;
import com.ari.wishlist.shared.data.UnitTestData;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class WishlistMapperTest {

    @Test
    void givenValidWishlist_whenToDTO_thenReturnsWishlistDTO() {
        Product product1 = UnitTestData.createProduct(UnitTestData.PRODUCT_ID_1, "Product 1", BigDecimal.valueOf(100.0));
        Product product2 = UnitTestData.createProduct(UnitTestData.PRODUCT_ID_2, "Product 2", BigDecimal.valueOf(150.0));
        Wishlist wishlist = UnitTestData.createWishlist(UnitTestData.CUSTOMER_ID_1, List.of(product1, product2));

        WishlistDTO wishlistDTO = WishlistMapper.toDTO(wishlist);

        assertNotNull(wishlistDTO);
        assertEquals(wishlist.getId(), wishlistDTO.getId());
        assertEquals(wishlist.getCustomerId(), wishlistDTO.getCustomerId());
        assertEquals(2, wishlistDTO.getProducts().size());
        assertEquals(product1.getProductId(), wishlistDTO.getProducts().get(0).getId());
        assertEquals(product2.getProductId(), wishlistDTO.getProducts().get(1).getId());
    }

    @Test
    void givenNullWishlist_whenToDTO_thenThrowsException() {
        var exception = assertThrows(WishlistMappingException.class,
                () -> WishlistMapper.toDTO(null));

        assertEquals("Wishlist cannot be null", exception.getMessage());
    }

    @Test
    void givenWishlistWithNullProducts_whenToDTO_thenReturnsWishlistDTOWithEmptyProducts() {
        Wishlist wishlist = UnitTestData.createWishlist(UnitTestData.CUSTOMER_ID_2, null);

        WishlistDTO wishlistDTO = WishlistMapper.toDTO(wishlist);

        assertNotNull(wishlistDTO);
        assertEquals(wishlist.getId(), wishlistDTO.getId());
        assertEquals(wishlist.getCustomerId(), wishlistDTO.getCustomerId());
        assertTrue(wishlistDTO.getProducts().isEmpty());
    }

    @Test
    void givenWishlistWithEmptyProducts_whenToDTO_thenReturnsWishlistDTOWithEmptyProducts() {
        Wishlist wishlist = UnitTestData.createEmptyWishlist(UnitTestData.CUSTOMER_ID_3);

        WishlistDTO wishlistDTO = WishlistMapper.toDTO(wishlist);

        assertNotNull(wishlistDTO);
        assertEquals(wishlist.getId(), wishlistDTO.getId());
        assertEquals(wishlist.getCustomerId(), wishlistDTO.getCustomerId());
        assertTrue(wishlistDTO.getProducts().isEmpty());
    }
}
