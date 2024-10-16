package com.ari.wishlist.application.usecase;

import com.ari.wishlist.domain.exception.ProductNotInWishlistException;
import com.ari.wishlist.domain.model.Product;
import com.ari.wishlist.domain.model.Wishlist;
import com.ari.wishlist.domain.repository.WishlistRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

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
        Product product = new Product(productId, "Product 1", 100.0);
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
        Product product = new Product("product-1", "Product 1", 150.0);
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

        Exception exception = assertThrows(ProductNotInWishlistException.class,
                () -> hasProductInWishlistUseCase.execute(customerId, productId));

        assertEquals(WISHLIST_NOT_FOUND_MESSAGE + customerId, exception.getMessage());
        verify(wishlistRepository).findByCustomerId(customerId);
    }
}
