package com.ari.wishlist.application.validator;

import com.ari.wishlist.domain.exception.ProductAlreadyInWishlistException;
import com.ari.wishlist.domain.exception.WishlistLimitExceededException;
import com.ari.wishlist.domain.model.Product;
import com.ari.wishlist.domain.model.Wishlist;
import com.ari.wishlist.infrastructure.configuration.WishlistConfig;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

class MaxProductsWishlistValidatorTest {

    private static final String WISHLIST_LIMIT_EXCEEDED_MESSAGE = "Wishlist cannot contain more than %d products";
    private static final String PRODUCT_ALREADY_IN_WISHLIST_MESSAGE = "Product is already in the wishlist";

    @Mock
    WishlistConfig wishlistConfig;

    @InjectMocks
    MaxProductsWishlistValidator maxProductsWishlistValidator;


    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void givenValidWishlistAndProduct_whenValidate_thenDoesNotThrow() {
        String productId = "product-1";
        BigDecimal price = BigDecimal.valueOf(100.0);
        Product product = Product.builder()
                .productId(productId)
                .name("Product 1")
                .price(price)
                .build();
        List<Product> products = new ArrayList<>();

        Wishlist wishlist = Wishlist.builder()
                .customerId("customer-1")
                .products(products)
                .build();

        when(wishlistConfig.getMaxProducts()).thenReturn(5);

        maxProductsWishlistValidator.validate(wishlist, product);

        verify(wishlistConfig).getMaxProducts();
    }

    @Test
    void givenWishlistExceedsMaxProducts_whenValidate_thenThrowsWishlistLimitExceededException() {
        String productId = "product-1";
        BigDecimal priceA = BigDecimal.valueOf(100.0);
        BigDecimal priceB = BigDecimal.valueOf(150.0);
        BigDecimal priceC = BigDecimal.valueOf(200.0);
        Product product = Product.builder()
                .productId(productId)
                .name("Product 1")
                .price(priceA)
                .build();
        List<Product> products = List.of(
                Product.builder()
                        .productId("product-2")
                        .name("Product 2")
                        .price(priceB)
                        .build(),
                Product.builder()
                        .productId("product-3")
                        .name("Product 3")
                        .price(priceC)
                        .build()
        );

        Wishlist wishlist = Wishlist.builder()
                .customerId("customer-1")
                .products(new ArrayList<>(products))
                .build();

        when(wishlistConfig.getMaxProducts()).thenReturn(2);

        WishlistLimitExceededException exception = assertThrows(WishlistLimitExceededException.class,
                () -> maxProductsWishlistValidator.validate(wishlist, product));

        assertEquals(String.format(WISHLIST_LIMIT_EXCEEDED_MESSAGE, 2), exception.getMessage());
    }

    @Test
    void givenProductAlreadyInWishlist_whenValidate_thenThrowsProductAlreadyInWishlistException() {
        String productId = "product-1";
        BigDecimal price = BigDecimal.valueOf(100.0);
        Product product = Product.builder()
                .productId(productId)
                .name("Product 1")
                .price(price)
                .build();
        List<Product> products = List.of(product);

        Wishlist wishlist = Wishlist.builder()
                .customerId("customer-1")
                .products(new ArrayList<>(products))
                .build();

        when(wishlistConfig.getMaxProducts()).thenReturn(5);

        ProductAlreadyInWishlistException exception = assertThrows(ProductAlreadyInWishlistException.class,
                () -> maxProductsWishlistValidator.validate(wishlist, product));

        assertEquals(PRODUCT_ALREADY_IN_WISHLIST_MESSAGE, exception.getMessage());
    }
}
