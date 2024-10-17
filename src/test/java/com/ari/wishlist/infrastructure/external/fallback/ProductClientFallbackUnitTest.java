package com.ari.wishlist.infrastructure.external.fallback;

import com.ari.wishlist.application.dto.ProductDTO;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.*;

class ProductClientFallbackTest {

    private final ProductClientFallback productClientFallback = new ProductClientFallback();

    @Test
    void givenProductById_WhenProductExists_ThenReturnProduct() {
        String productId = "product-5";
        BigDecimal price = BigDecimal.valueOf(110.0);
        ProductDTO product = productClientFallback.getProductById(productId);

        assertNotNull(product);
        assertEquals(productId, product.getId());
        assertEquals("Product 5", product.getName());
        assertEquals(price, product.getPrice());
    }

    @Test
    void givenProductById_WhenProductDoesNotExist_ThenReturnUnknownProduct() {
        String productId = "invalid-product";
        ProductDTO product = productClientFallback.getProductById(productId);

        assertNotNull(product);
        assertEquals("unknown", product.getId());
        assertEquals("Product 1", productClientFallback.getProductById("product-1").getName());
    }
}
