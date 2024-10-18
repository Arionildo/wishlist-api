package com.ari.wishlist.application.usecase;

import com.ari.wishlist.application.dto.ProductDTO;
import com.ari.wishlist.infrastructure.external.ProductClient;
import com.ari.wishlist.infrastructure.external.exception.ProductNotFoundException;
import com.ari.wishlist.shared.data.UnitTestData;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class GetProductByIdUseCaseTest {

    private static final String PRODUCT_NOT_FOUND_MESSAGE = "Product with ID invalid-product-1 not found.";

    @Mock
    private ProductClient productClient;

    @InjectMocks
    private GetProductByIdUseCase productService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void givenProductById_WhenProductExists_ThenReturnProduct() {
        String productId = UnitTestData.PRODUCT_ID_21;
        BigDecimal price = BigDecimal.valueOf(100.0);
        ProductDTO mockProduct = UnitTestData.createProductDTO(productId, "Test Product", price);

        when(productClient.getProductById(productId)).thenReturn(mockProduct);

        ProductDTO product = productService.execute(productId);

        assertNotNull(product);
        assertEquals(productId, product.getId());
        assertEquals("Test Product", product.getName());
        assertEquals(price, product.getPrice());

        verify(productClient, times(1)).getProductById(productId);
    }

    @Test
    void givenProductById_WhenProductDoesNotExist_ThenThrowProductNotFoundException() {
        String productId = "invalid-product-1";
        BigDecimal price = BigDecimal.ZERO;
        ProductDTO mockProduct = UnitTestData.createProductDTO("unknown", "Unknown Product", price);

        when(productClient.getProductById(productId)).thenReturn(mockProduct);

        ProductNotFoundException exception = assertThrows(ProductNotFoundException.class, () -> productService.execute(productId));
        assertEquals(PRODUCT_NOT_FOUND_MESSAGE, exception.getMessage());

        verify(productClient, times(1)).getProductById(productId);
    }
}
