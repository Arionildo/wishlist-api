package com.ari.wishlist.application.usecase;

import com.ari.wishlist.application.dto.ProductDTO;
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
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class GetAllProductsFromWishlistUseCaseTest {

    private static final String WISHLIST_NOT_FOUND_MESSAGE = "Wishlist not found for customer ID: ";

    @Mock
    WishlistRepository wishlistRepository;

    @InjectMocks
    GetAllProductsFromWishlistUseCase getAllProductsFromWishlistUseCase;

    @Test
    void givenValidCustomerId_whenExecute_thenReturnsProductList() {
        String customerId = "customer-1";
        BigDecimal price = BigDecimal.valueOf(100.0);
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

        List<ProductDTO> result = getAllProductsFromWishlistUseCase.execute(customerId);

        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals("product-1", result.get(0).getId());
        verify(wishlistRepository).findByCustomerId(customerId);
    }

    @Test
    void givenInvalidCustomerId_whenExecute_thenThrowsException() {
        String customerId = "invalid-customer";

        when(wishlistRepository.findByCustomerId(customerId)).thenReturn(Optional.empty());

        Exception exception = assertThrows(WishlistNotFoundException.class,
                () -> getAllProductsFromWishlistUseCase.execute(customerId));

        assertEquals(WISHLIST_NOT_FOUND_MESSAGE + customerId, exception.getMessage());
        verify(wishlistRepository).findByCustomerId(customerId);
    }
}
