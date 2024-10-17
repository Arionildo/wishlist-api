package com.ari.wishlist.application.usecase;

import com.ari.wishlist.domain.exception.ProductNotInWishlistException;
import com.ari.wishlist.domain.exception.WishlistNotFoundException;
import com.ari.wishlist.domain.model.Product;
import com.ari.wishlist.domain.model.Wishlist;
import com.ari.wishlist.domain.repository.WishlistRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class HasProductInWishlistUseCaseTest {

    private static final String WISHLIST_NOT_FOUND_MESSAGE = "Wishlist not found for customer ID: ";

    @Mock
    WishlistRepository wishlistRepository;

    @InjectMocks
    HasProductInWishlistUseCase hasProductInWishlistUseCase;

    @Test
    void givenValidCustomerIdAndProductId_whenProductExistsInWishlist_thenReturnsTrue() {
        String customerId = "customer-1";
        String productId = "product-1";
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
        String customerId = "customer-2";
        String productId = "product-2";
        BigDecimal price = BigDecimal.valueOf(150.0);
        Product product = Product.builder()
                .productId("product-1")
                .name("Product 1")
                .price(price)
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
        String customerId = "customer-3";
        String productId = "product-3";

        when(wishlistRepository.findByCustomerId(customerId)).thenReturn(Optional.empty());

        Exception exception = assertThrows(WishlistNotFoundException.class,
                () -> hasProductInWishlistUseCase.execute(customerId, productId));

        assertEquals(WISHLIST_NOT_FOUND_MESSAGE + customerId, exception.getMessage());
        verify(wishlistRepository).findByCustomerId(customerId);
    }
}
