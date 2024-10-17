package com.ari.wishlist.application.mapper;

import com.ari.wishlist.application.dto.ProductDTO;
import com.ari.wishlist.application.exception.ProductMappingException;
import com.ari.wishlist.domain.model.Product;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.*;

class ProductMapperTest {

    @Test
    void givenValidProductDTO_whenToDomain_thenReturnsProduct() {
        BigDecimal price = BigDecimal.valueOf(100.0);
        var productDTO = ProductDTO.builder()
                .id("product-1")
                .name("Product 1")
                .price(price)
                .build();

        var product = ProductMapper.toDomain(productDTO);

        assertNotNull(product);
        assertEquals("product-1", product.getProductId());
        assertEquals("Product 1", product.getName());
        assertEquals(price, product.getPrice());
    }

    @Test
    void givenNullProductDTO_whenToDomain_thenThrowsException() {
        var exception = assertThrows(ProductMappingException.class,
                () -> ProductMapper.toDomain(null));

        assertEquals("DTO cannot be null", exception.getMessage());
    }

    @Test
    void givenValidProduct_whenToDTO_thenReturnsProductDTO() {
        BigDecimal price = BigDecimal.valueOf(100.0);
        var product = Product.builder()
                .productId("product-1")
                .name("Product 1")
                .price(price)
                .build();

        ProductDTO productDTO = ProductMapper.toDTO(product);

        assertNotNull(productDTO);
        assertEquals("product-1", productDTO.getId());
        assertEquals("Product 1", productDTO.getName());
        assertEquals(price, productDTO.getPrice());
    }

    @Test
    void givenNullProduct_whenToDTO_thenThrowsException() {
        var exception = assertThrows(ProductMappingException.class,
                () -> ProductMapper.toDTO(null));

        assertEquals("Product cannot be null", exception.getMessage());
    }
}
