package com.ari.wishlist.infrastructure.external.fallback;

import com.ari.wishlist.application.dto.ProductDTO;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class ProductClientFallbackUnitTest {

    private final ProductClientFallback productClientFallback = new ProductClientFallback();

    @Test
    void getProductById_WhenProductExists_ShouldReturnProduct() {
        String productId = "product-5";
        ProductDTO product = productClientFallback.getProductById(productId);

        assertNotNull(product);
        assertEquals(productId, product.getId());
        assertEquals("Product 5", product.getName());
        assertEquals(110.0, product.getPrice());
    }

    @Test
    void getProductById_WhenProductDoesNotExist_ShouldReturnUnknownProduct() {
        String productId = "invalid-product";
        ProductDTO product = productClientFallback.getProductById(productId);

        assertNotNull(product);
        assertEquals("unknown", product.getId());
        assertEquals("Product 1", productClientFallback.getProductById("product-1").getName());
    }
}
