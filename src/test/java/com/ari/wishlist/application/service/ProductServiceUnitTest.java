package com.ari.wishlist.application.service;

import com.ari.wishlist.application.dto.ProductDTO;
import com.ari.wishlist.infrastructure.external.ProductClient;
import com.ari.wishlist.infrastructure.external.exception.ProductNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class ProductServiceUnitTest {

    @Mock
    private ProductClient productClient;

    @InjectMocks
    private ProductService productService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void getProductById_WhenProductExists_ShouldReturnProduct() {
        String productId = "product-21";
        ProductDTO mockProduct = ProductDTO.builder()
                .id(productId)
                .name("Test Product")
                .price(100.0)
                .build();

        when(productClient.getProductById(productId)).thenReturn(mockProduct);

        ProductDTO product = productService.getProductById(productId);

        assertNotNull(product);
        assertEquals(productId, product.getId());
        assertEquals("Test Product", product.getName());
        assertEquals(100.0, product.getPrice());

        verify(productClient, times(1)).getProductById(productId);
    }

    @ParameterizedTest
    @MethodSource("provideProductIds")
    void getProductById_WhenProductDoesNotExist_ShouldThrowProductNotFoundException(String productId, String expectedMessage) {
        ProductDTO mockProduct = ProductDTO.builder()
                .id("unknown")
                .name("Unknown Product")
                .price(0.0)
                .build();

        when(productClient.getProductById(productId)).thenReturn(mockProduct);

        ProductNotFoundException exception = assertThrows(ProductNotFoundException.class, () -> productService.getProductById(productId));
        assertEquals(expectedMessage, exception.getMessage());

        verify(productClient, times(1)).getProductById(productId);
    }

    private static Stream<Arguments> provideProductIds() {
        return Stream.of(
                Arguments.of("invalid-product-1", "Product with ID invalid-product-1 not found."),
                Arguments.of("invalid-product-2", "Product with ID invalid-product-2 not found."),
                Arguments.of("invalid-product-3", "Product with ID invalid-product-3 not found.")
        );
    }
}
