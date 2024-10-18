package com.ari.wishlist.application.usecase;

import com.ari.wishlist.application.dto.ProductDTO;
import com.ari.wishlist.application.mapper.ProductMapper;
import com.ari.wishlist.application.validator.WishlistValidator;
import com.ari.wishlist.domain.exception.ProductAlreadyInWishlistException;
import com.ari.wishlist.domain.exception.ProductNotInWishlistException;
import com.ari.wishlist.domain.exception.WishlistLimitExceededException;
import com.ari.wishlist.domain.model.Product;
import com.ari.wishlist.domain.model.Wishlist;
import com.ari.wishlist.shared.config.UnitTestConfig;
import com.ari.wishlist.shared.data.UnitTestData;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class AddProductToWishlistUseCaseTest extends UnitTestConfig {

    @Mock
    WishlistValidator wishlistValidator;

    @Mock
    GetProductByIdUseCase getProductByIdUseCase;

    @InjectMocks
    AddProductToWishlistUseCase addProductToWishlistUseCase;

    @Test
    void givenValidCustomerIdAndProductId_whenExecute_thenAddsProductToWishlist() {
        String customerId = UnitTestData.CUSTOMER_ID_1;
        String productId = UnitTestData.PRODUCT_ID_1;
        BigDecimal price = BigDecimal.valueOf(100.0);
        ProductDTO productDTO = UnitTestData.createProductDTO(productId, "Product 1", price);
        Product product = ProductMapper.toDomain(productDTO);
        Wishlist wishlist = UnitTestData.createWishlist(customerId, new ArrayList<>());

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
        String customerId = UnitTestData.CUSTOMER_ID_2;
        String productId = UnitTestData.PRODUCT_ID_2;
        BigDecimal price = BigDecimal.valueOf(150.0);
        ProductDTO productDTO = UnitTestData.createProductDTO(productId, "Product 2", price);

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
        String customerId = UnitTestData.CUSTOMER_ID_3;
        String productId = UnitTestData.INVALID_PRODUCT_ID;

        when(getProductByIdUseCase.execute(productId)).thenThrow(new ProductNotInWishlistException(UnitTestData.PRODUCT_NOT_FOUND_MESSAGE));

        Exception exception = assertThrows(ProductNotInWishlistException.class,
                () -> addProductToWishlistUseCase.execute(customerId, productId));

        assertEquals(UnitTestData.PRODUCT_NOT_FOUND_MESSAGE, exception.getMessage());
        verify(wishlistRepository, never()).save(any());
    }

    @Test
    void givenCustomerId_whenProductAlreadyExistsInWishlist_thenThrowsException() {
        String customerId = UnitTestData.CUSTOMER_ID_1;
        String productId = UnitTestData.PRODUCT_ID_1;
        ProductDTO productDTO = UnitTestData.createProductDTO(productId, "Product 1", BigDecimal.valueOf(100.0));
        Product product = ProductMapper.toDomain(productDTO);

        Wishlist wishlist = UnitTestData.createWishlist(customerId, List.of(product));

        when(getProductByIdUseCase.execute(productId)).thenReturn(productDTO);
        when(wishlistRepository.findByCustomerId(customerId)).thenReturn(Optional.of(wishlist));

        doThrow(new ProductAlreadyInWishlistException(UnitTestData.ALREADY_IN_WISHLIST_MESSAGE))
                .when(wishlistValidator).validate(any(), any());

        Exception exception = assertThrows(ProductAlreadyInWishlistException.class,
                () -> addProductToWishlistUseCase.execute(customerId, productId));

        assertEquals(UnitTestData.ALREADY_IN_WISHLIST_MESSAGE, exception.getMessage());
        verify(wishlistValidator).validate(wishlist, product);
        verify(wishlistRepository, never()).save(any());
    }

    @Test
    void givenCustomerId_whenWishlistIsFull_thenThrowsException() {
        String customerId = "customer-5";
        String productId = UnitTestData.PRODUCT_ID_1;
        BigDecimal priceA = BigDecimal.valueOf(250.0);
        BigDecimal priceB = BigDecimal.valueOf(300.0);
        ProductDTO productDTO = UnitTestData.createProductDTO(productId, "Product 1", priceA);
        Product product = UnitTestData.createProduct("existing-product", "Existing Product", priceB);

        Wishlist wishlist = UnitTestData.createWishlist(customerId, List.of(product));

        when(getProductByIdUseCase.execute(productId)).thenReturn(productDTO);
        when(wishlistRepository.findByCustomerId(customerId)).thenReturn(Optional.of(wishlist));

        doThrow(new WishlistLimitExceededException(UnitTestData.WISHLIST_FULL_MESSAGE))
                .when(wishlistValidator).validate(any(Wishlist.class), any(Product.class));

        Exception exception = assertThrows(WishlistLimitExceededException.class,
                () -> addProductToWishlistUseCase.execute(customerId, productId));

        assertEquals(UnitTestData.WISHLIST_FULL_MESSAGE, exception.getMessage());
        verify(wishlistRepository, never()).save(any());
    }
}
