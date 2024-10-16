package com.ari.wishlist.application.usecase;

import com.ari.wishlist.application.dto.ProductDTO;
import com.ari.wishlist.application.mapper.ProductMapper;
import com.ari.wishlist.application.validator.WishlistValidator;
import com.ari.wishlist.domain.exception.ProductNotInWishlistException;
import com.ari.wishlist.domain.exception.ProductAlreadyInWishlistException;
import com.ari.wishlist.domain.exception.WishlistLimitExceededException;
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
class AddProductToWishlistUseCaseTest {

    private static final String PRODUCT_NOT_FOUND_MESSAGE = "Product not found";
    private static final String ALREADY_IN_WISHLIST_MESSAGE = "Product is already in the wishlist";
    private static final String WISHLIST_FULL_MESSAGE = "Wishlist cannot contain more than 1 products";

    @Mock
    WishlistRepository wishlistRepository;

    @Mock
    WishlistValidator wishlistValidator;

    @Mock
    GetProductByIdUseCase getProductByIdUseCase;

    @InjectMocks
    AddProductToWishlistUseCase addProductToWishlistUseCase;

    @Test
    void givenValidCustomerIdAndProductId_whenExecute_thenAddsProductToWishlist() {
        String customerId = "customer-1";
        String productId = "product-1";
        ProductDTO productDTO = new ProductDTO(productId, "Product 1", 100.0);
        Product product = ProductMapper.toDomain(productDTO);
        Wishlist wishlist = Wishlist.builder().customerId(customerId).products(new ArrayList<>()).build();

        when(getProductByIdUseCase.execute(productId)).thenReturn(productDTO);
        when(wishlistRepository.findByCustomerId(customerId)).thenReturn(Optional.of(wishlist));
        when(wishlistRepository.save(any())).thenReturn(wishlist);

        Wishlist result = addProductToWishlistUseCase.execute(customerId, productId);

        assertNotNull(result.getProducts());
        assertEquals(1, result.getProducts().size());
        assertEquals(productId, result.getProducts().get(0).getProductId());
        verify(wishlistValidator).validate(wishlist, product);
        verify(wishlistRepository).save(wishlist);
    }

    @Test
    void givenNewCustomerId_whenExecute_thenCreatesNewWishlist() {
        String customerId = "customer-2";
        String productId = "product-2";
        ProductDTO productDTO = new ProductDTO(productId, "Product 2", 150.0);

        when(getProductByIdUseCase.execute(productId)).thenReturn(productDTO);
        when(wishlistRepository.findByCustomerId(customerId)).thenReturn(Optional.empty());
        when(wishlistRepository.save(any())).thenAnswer(invocation -> invocation.getArgument(0));

        Wishlist result = addProductToWishlistUseCase.execute(customerId, productId);

        assertNotNull(result);
        assertEquals(customerId, result.getCustomerId());
        assertEquals(1, result.getProducts().size());
        assertEquals(productId, result.getProducts().get(0).getProductId());
        verify(wishlistRepository).save(any(Wishlist.class));
    }

    @Test
    void givenInvalidProductId_whenExecute_thenThrowsException() {
        String customerId = "customer-3";
        String productId = "invalid-product";

        when(getProductByIdUseCase.execute(productId)).thenThrow(new ProductNotInWishlistException(PRODUCT_NOT_FOUND_MESSAGE));

        Exception exception = assertThrows(ProductNotInWishlistException.class,
                () -> addProductToWishlistUseCase.execute(customerId, productId));

        assertEquals(PRODUCT_NOT_FOUND_MESSAGE, exception.getMessage());
        verify(wishlistRepository, never()).save(any());
    }

    @Test
    void givenCustomerId_whenProductAlreadyExistsInWishlist_thenThrowsException() {
        String customerId = "customer-1";
        String productId = "product-1";
        ProductDTO productDTO = new ProductDTO(productId, "Product 1", 100.0);
        Product product = ProductMapper.toDomain(productDTO);

        Wishlist wishlist = Wishlist.builder()
                .customerId(customerId)
                .products(List.of(product))
                .build();

        when(getProductByIdUseCase.execute(productId)).thenReturn(productDTO);
        when(wishlistRepository.findByCustomerId(customerId)).thenReturn(Optional.of(wishlist));

        doThrow(new ProductAlreadyInWishlistException(ALREADY_IN_WISHLIST_MESSAGE))
                .when(wishlistValidator).validate(any(), any());

        Exception exception = assertThrows(ProductAlreadyInWishlistException.class,
                () -> addProductToWishlistUseCase.execute(customerId, productId));

        assertEquals(ALREADY_IN_WISHLIST_MESSAGE, exception.getMessage());
        verify(wishlistValidator).validate(wishlist, product);
        verify(wishlistRepository, never()).save(any());
    }

    @Test
    void givenCustomerId_whenWishlistIsFull_thenThrowsException() {
        String customerId = "customer-5";
        String productId = "product-1";
        ProductDTO productDTO = new ProductDTO(productId, "Product 1", 250.0);

        Wishlist wishlist = Wishlist.builder()
                .customerId(customerId)
                .products(new ArrayList<>(List.of(new Product("existing-product", "Existing Product", 300.0))))
                .build();

        when(getProductByIdUseCase.execute(productId)).thenReturn(productDTO);
        when(wishlistRepository.findByCustomerId(customerId)).thenReturn(Optional.of(wishlist));

        doThrow(new WishlistLimitExceededException(WISHLIST_FULL_MESSAGE))
                .when(wishlistValidator).validate(any(Wishlist.class), any(Product.class));

        Exception exception = assertThrows(WishlistLimitExceededException.class,
                () -> addProductToWishlistUseCase.execute(customerId, productId));

        assertEquals(WISHLIST_FULL_MESSAGE, exception.getMessage());
        verify(wishlistRepository, never()).save(any());
    }
}
