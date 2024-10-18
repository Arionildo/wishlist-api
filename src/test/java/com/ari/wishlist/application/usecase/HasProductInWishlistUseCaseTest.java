package com.ari.wishlist.application.usecase;

import com.ari.wishlist.domain.exception.WishlistNotFoundException;
import com.ari.wishlist.domain.model.Product;
import com.ari.wishlist.domain.model.Wishlist;
import com.ari.wishlist.shared.config.UnitTestConfig;
import com.ari.wishlist.shared.data.UnitTestData;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

class HasProductInWishlistUseCaseTest extends UnitTestConfig {

    @InjectMocks
    HasProductInWishlistUseCase hasProductInWishlistUseCase;

    @Test
    void givenValidCustomerIdAndProductId_whenProductExistsInWishlist_thenReturnsTrue() {
        String customerId = UnitTestData.CUSTOMER_ID_1;
        String productId = UnitTestData.PRODUCT_ID_1;
        BigDecimal price = BigDecimal.valueOf(100.0);
        Product product = Product.builder()
                .productId(productId)
                .name("Product 1")
                .price(price)
                .build();
        Wishlist wishlist = Wishlist.builder()
                .customerId(customerId)
                .products(List.of(product))
                .build();

        when(wishlistRepository.findByCustomerId(customerId)).thenReturn(Optional.of(wishlist));

        boolean result = hasProductInWishlistUseCase.execute(customerId, productId);

        assertTrue(result);
        verify(wishlistRepository).findByCustomerId(customerId);
    }

    @Test
    void givenValidCustomerIdAndProductId_whenProductDoesNotExistInWishlist_thenReturnsFalse() {
        String customerId = UnitTestData.CUSTOMER_ID_2;
        String productId = UnitTestData.PRODUCT_ID_2;
        Product product = Product.builder()
                .productId(UnitTestData.PRODUCT_ID_1)
                .name("Product 1")
                .price(BigDecimal.valueOf(150.0))
                .build();
        Wishlist wishlist = Wishlist.builder()
                .customerId(customerId)
                .products(List.of(product))
                .build();

        when(wishlistRepository.findByCustomerId(customerId)).thenReturn(Optional.of(wishlist));

        boolean result = hasProductInWishlistUseCase.execute(customerId, productId);

        assertFalse(result);
        verify(wishlistRepository).findByCustomerId(customerId);
    }

    @Test
    void givenInvalidCustomerId_whenWishlistNotFound_thenThrowsException() {
        String customerId = UnitTestData.CUSTOMER_ID_3;
        String productId = UnitTestData.PRODUCT_ID_3;

        when(wishlistRepository.findByCustomerId(customerId)).thenReturn(Optional.empty());

        WishlistNotFoundException exception = assertThrows(WishlistNotFoundException.class,
                () -> hasProductInWishlistUseCase.execute(customerId, productId));

        assertEquals(UnitTestData.WISHLIST_NOT_FOUND_CUSTOMER_MESSAGE + customerId, exception.getMessage());
        verify(wishlistRepository).findByCustomerId(customerId);
    }
}
