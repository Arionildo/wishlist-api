package com.ari.wishlist.application.usecase;

import com.ari.wishlist.application.dto.ProductDTO;
import com.ari.wishlist.domain.exception.EmptyWishlistException;
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

class GetAllProductsFromWishlistUseCaseTest extends UnitTestConfig {

    private static final String WISHLIST_NOT_FOUND_MESSAGE = "Wishlist not found for customer ID: ";
    private static final String EMPTY_WISHLIST_MESSAGE_FORMAT = "Wishlist is empty for customer ID: %s";

    @InjectMocks
    GetAllProductsFromWishlistUseCase getAllProductsFromWishlistUseCase;

    @Test
    void givenValidCustomerId_whenExecute_thenReturnsProductList() {
        String customerId = UnitTestData.CUSTOMER_ID_1;
        Product product = UnitTestData.createProduct(UnitTestData.PRODUCT_ID_1, "Product 1", BigDecimal.valueOf(100.0));
        Wishlist wishlist = UnitTestData.createWishlist(customerId, List.of(product));

        when(wishlistRepository.findByCustomerId(customerId)).thenReturn(Optional.of(wishlist));

        List<ProductDTO> result = getAllProductsFromWishlistUseCase.execute(customerId);

        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals(UnitTestData.PRODUCT_ID_1, result.get(0).getId());
        verify(wishlistRepository).findByCustomerId(customerId);
    }

    @Test
    void givenInvalidCustomerId_whenExecute_thenThrowsException() {
        String customerId = UnitTestData.INVALID_CUSTOMER_ID;

        when(wishlistRepository.findByCustomerId(customerId)).thenReturn(Optional.empty());

        Exception exception = assertThrows(WishlistNotFoundException.class,
                () -> getAllProductsFromWishlistUseCase.execute(customerId));

        assertEquals(WISHLIST_NOT_FOUND_MESSAGE + customerId, exception.getMessage());
        verify(wishlistRepository).findByCustomerId(customerId);
    }

    @Test
    void givenEmptyWishlist_whenExecute_thenThrowsException() {
        String customerId = UnitTestData.CUSTOMER_ID_2;
        Wishlist wishlist = UnitTestData.createEmptyWishlist(customerId);

        when(wishlistRepository.findByCustomerId(customerId)).thenReturn(Optional.of(wishlist));

        Exception exception = assertThrows(EmptyWishlistException.class,
                () -> getAllProductsFromWishlistUseCase.execute(customerId));

        assertEquals(String.format(EMPTY_WISHLIST_MESSAGE_FORMAT, customerId), exception.getMessage());
        verify(wishlistRepository).findByCustomerId(customerId);
    }
}
