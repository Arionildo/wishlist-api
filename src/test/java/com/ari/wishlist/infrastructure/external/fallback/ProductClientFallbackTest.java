package com.ari.wishlist.infrastructure.external.fallback;

import com.ari.wishlist.application.dto.ProductDTO;
import com.ari.wishlist.shared.data.UnitTestData;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.*;

class ProductClientFallbackTest {

    private final ProductClientFallback productClientFallback = new ProductClientFallback();

    @Test
    void givenProductById_WhenProductExists_ThenReturnProduct() {
        String productId = UnitTestData.PRODUCT_ID_1;
        BigDecimal price = BigDecimal.valueOf(102.0);
        ProductDTO product = productClientFallback.getProductById(productId);

        assertNotNull(product);
        assertEquals(productId, product.getId());
        assertEquals("Product 1", product.getName());
        assertEquals(price, product.getPrice());
    }

    @Test
    void givenProductById_WhenProductDoesNotExist_ThenReturnUnknownProduct() {
        String productId = UnitTestData.INVALID_PRODUCT_ID;
        ProductDTO product = productClientFallback.getProductById(productId);

        assertNotNull(product);
        assertEquals("unknown", product.getId());
        assertEquals("Product 1", productClientFallback.getProductById(UnitTestData.PRODUCT_ID_1).getName());
    }
}
