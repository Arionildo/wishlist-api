package com.ari.wishlist.application.usecase;

import com.ari.wishlist.domain.exception.ProductNotInWishlistException;
import com.ari.wishlist.domain.exception.WishlistNotFoundException;
import com.ari.wishlist.domain.model.Product;
import com.ari.wishlist.domain.model.Wishlist;
import com.ari.wishlist.domain.repository.WishlistRepository;
import com.ari.wishlist.shared.config.UnitTestConfig;
import com.ari.wishlist.shared.data.UnitTestData;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class RemoveProductFromWishlistUseCaseTest extends UnitTestConfig {

    @InjectMocks
    RemoveProductFromWishlistUseCase removeProductFromWishlistUseCase;

    @Test
    void givenValidCustomerIdAndProductId_whenProductExistsInWishlist_thenRemovesProduct() {
        String customerId = UnitTestData.CUSTOMER_ID_1;
        String productId = UnitTestData.PRODUCT_ID_1;
        Product product = UnitTestData.createProduct(productId, "Product 1", BigDecimal.valueOf(100.0));
        Wishlist wishlist = UnitTestData.createWishlist(customerId, new ArrayList<>(List.of(product)));

        when(wishlistRepository.findByCustomerId(customerId)).thenReturn(Optional.of(wishlist));
        when(wishlistRepository.save(any())).thenAnswer(invocation -> invocation.getArgument(0));

        Wishlist result = removeProductFromWishlistUseCase.execute(customerId, productId);

        assertTrue(result.getProducts().isEmpty());
        verify(wishlistRepository).findByCustomerId(customerId);
        verify(wishlistRepository).save(wishlist);
    }

    @Test
    void givenValidCustomerIdAndProductId_whenProductDoesNotExistInWishlist_thenThrowsProductNotInWishlistException() {
        String customerId = UnitTestData.CUSTOMER_ID_2;
        String productId = UnitTestData.PRODUCT_ID_2;
        Product product = UnitTestData.createProduct(UnitTestData.PRODUCT_ID_1, "Product 1", BigDecimal.valueOf(150.0));
        Wishlist wishlist = UnitTestData.createWishlist(customerId, new ArrayList<>(List.of(product)));

        when(wishlistRepository.findByCustomerId(customerId)).thenReturn(Optional.of(wishlist));

        ProductNotInWishlistException exception = assertThrows(ProductNotInWishlistException.class,
                () -> removeProductFromWishlistUseCase.execute(customerId, productId));

        assertEquals(UnitTestData.PRODUCT_NOT_FOUND_IN_WISHLIST_MESSAGE, exception.getMessage());
        verify(wishlistRepository).findByCustomerId(customerId);
        verify(wishlistRepository, never()).save(any());
    }

    @Test
    void givenInvalidCustomerId_whenWishlistNotFound_thenThrowsWishlistNotFoundException() {
        String customerId = UnitTestData.CUSTOMER_ID_3;
        String productId = UnitTestData.PRODUCT_ID_3;

        when(wishlistRepository.findByCustomerId(customerId)).thenReturn(Optional.empty());

        WishlistNotFoundException exception = assertThrows(WishlistNotFoundException.class,
                () -> removeProductFromWishlistUseCase.execute(customerId, productId));

        assertEquals(UnitTestData.WISHLIST_NOT_FOUND_MESSAGE, exception.getMessage());
        verify(wishlistRepository).findByCustomerId(customerId);
        verify(wishlistRepository, never()).save(any());
    }

    @Test
    void givenValidCustomerId_whenRemovingProductFromEmptyWishlist_thenThrowsProductNotInWishlistException() {
        String customerId = UnitTestData.CUSTOMER_ID_1;
        String productId = UnitTestData.PRODUCT_ID_3;

        Wishlist wishlist = UnitTestData.createEmptyWishlist(customerId);

        when(wishlistRepository.findByCustomerId(customerId)).thenReturn(Optional.of(wishlist));

        ProductNotInWishlistException exception = assertThrows(ProductNotInWishlistException.class,
                () -> removeProductFromWishlistUseCase.execute(customerId, productId));

        assertEquals(UnitTestData.PRODUCT_NOT_FOUND_IN_WISHLIST_MESSAGE, exception.getMessage());
        verify(wishlistRepository).findByCustomerId(customerId);
        verify(wishlistRepository, never()).save(any());
    }
}
