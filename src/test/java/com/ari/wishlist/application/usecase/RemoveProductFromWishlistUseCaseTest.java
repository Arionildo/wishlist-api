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

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class RemoveProductFromWishlistUseCaseTest {

    private static final String WISHLIST_NOT_FOUND_MESSAGE = "Wishlist not found";

    @Mock
    WishlistRepository wishlistRepository;

    @InjectMocks
    RemoveProductFromWishlistUseCase removeProductFromWishlistUseCase;

    @Test
    void givenValidCustomerIdAndProductId_whenProductExistsInWishlist_thenRemovesProduct() {
        String customerId = "customer-1";
        String productId = "product-1";
        Product product = new Product(productId, "Product 1", 100.0);
        Wishlist wishlist = Wishlist.builder()
                .customerId(customerId)
                .products(new ArrayList<>(List.of(product)))
                .build();

        when(wishlistRepository.findByCustomerId(customerId)).thenReturn(Optional.of(wishlist));
        when(wishlistRepository.save(any())).thenAnswer(invocation -> invocation.getArgument(0));

        Wishlist result = removeProductFromWishlistUseCase.execute(customerId, productId);

        assertTrue(result.getProducts().isEmpty());
        verify(wishlistRepository).findByCustomerId(customerId);
        verify(wishlistRepository).save(wishlist);
    }

    @Test
    void givenValidCustomerIdAndProductId_whenProductDoesNotExistInWishlist_thenThrowsProductNotInWishlistException() {
        String customerId = "customer-2";
        String productId = "product-2";
        Product product = new Product("product-1", "Product 1", 150.0);
        Wishlist wishlist = Wishlist.builder()
                .customerId(customerId)
                .products(new ArrayList<>(List.of(product)))
                .build();

        when(wishlistRepository.findByCustomerId(customerId)).thenReturn(Optional.of(wishlist));

        ProductNotInWishlistException exception = assertThrows(ProductNotInWishlistException.class,
                () -> removeProductFromWishlistUseCase.execute(customerId, productId));

        assertEquals("Product not found in wishlist", exception.getMessage());
        verify(wishlistRepository).findByCustomerId(customerId);
        verify(wishlistRepository, never()).save(any());
    }

    @Test
    void givenInvalidCustomerId_whenWishlistNotFound_thenThrowsWishlistNotFoundException() {
        String customerId = "customer-3";
        String productId = "product-3";

        when(wishlistRepository.findByCustomerId(customerId)).thenReturn(Optional.empty());

        WishlistNotFoundException exception = assertThrows(WishlistNotFoundException.class,
                () -> removeProductFromWishlistUseCase.execute(customerId, productId));

        assertEquals(WISHLIST_NOT_FOUND_MESSAGE, exception.getMessage());
        verify(wishlistRepository).findByCustomerId(customerId);
        verify(wishlistRepository, never()).save(any());
    }
}
