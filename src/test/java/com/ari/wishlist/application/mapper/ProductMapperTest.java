package com.ari.wishlist.application.mapper;

import com.ari.wishlist.application.dto.ProductDTO;
import com.ari.wishlist.application.exception.ProductMappingException;
import com.ari.wishlist.shared.data.UnitTestData;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.*;

class ProductMapperTest {

    @Test
    void givenValidProductDTO_whenToDomain_thenReturnsProduct() {
        var productDTO = UnitTestData.createProductDTO(UnitTestData.PRODUCT_ID_1, "Product 1", BigDecimal.valueOf(100.0));

        var product = ProductMapper.toDomain(productDTO);

        assertNotNull(product);
        assertEquals(UnitTestData.PRODUCT_ID_1, product.getProductId());
        assertEquals("Product 1", product.getName());
        assertEquals(BigDecimal.valueOf(100.0), product.getPrice());
    }

    @Test
    void givenNullProductDTO_whenToDomain_thenThrowsException() {
        var exception = assertThrows(ProductMappingException.class,
                () -> ProductMapper.toDomain(null));

        assertEquals("DTO cannot be null", exception.getMessage());
    }

    @Test
    void givenValidProduct_whenToDTO_thenReturnsProductDTO() {
        var product = UnitTestData.createProduct(UnitTestData.PRODUCT_ID_1, "Product 1", BigDecimal.valueOf(100.0));

        ProductDTO productDTO = ProductMapper.toDTO(product);

        assertNotNull(productDTO);
        assertEquals(UnitTestData.PRODUCT_ID_1, productDTO.getId());
        assertEquals("Product 1", productDTO.getName());
        assertEquals(BigDecimal.valueOf(100.0), productDTO.getPrice());
    }

    @Test
    void givenNullProduct_whenToDTO_thenThrowsException() {
        var exception = assertThrows(ProductMappingException.class,
                () -> ProductMapper.toDTO(null));

        assertEquals("Product cannot be null", exception.getMessage());
    }
}
