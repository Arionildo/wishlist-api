package com.ari.wishlist.application.mapper;

import com.ari.wishlist.application.dto.ProductDTO;
import com.ari.wishlist.application.exception.ProductMappingException;
import com.ari.wishlist.domain.model.Product;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class ProductMapperTest {

    @Test
    void givenValidProductDTO_whenToDomain_thenReturnsProduct() {
        ProductDTO productDTO = new ProductDTO("product-1", "Product 1", 100.0);

        Product product = ProductMapper.toDomain(productDTO);

        assertNotNull(product);
        assertEquals("product-1", product.getProductId());
        assertEquals("Product 1", product.getName());
        assertEquals(100.0, product.getPrice());
    }

    @Test
    void givenNullProductDTO_whenToDomain_thenThrowsException() {
        Exception exception = assertThrows(ProductMappingException.class,
                () -> ProductMapper.toDomain(null));

        assertEquals("ProductDTO cannot be null", exception.getMessage());
    }

    @Test
    void givenValidProduct_whenToDTO_thenReturnsProductDTO() {
        Product product = new Product("product-1", "Product 1", 100.0);

        ProductDTO productDTO = ProductMapper.toDTO(product);

        assertNotNull(productDTO);
        assertEquals("product-1", productDTO.getId());
        assertEquals("Product 1", productDTO.getName());
        assertEquals(100.0, productDTO.getPrice());
    }

    @Test
    void givenNullProduct_whenToDTO_thenThrowsException() {
        Exception exception = assertThrows(ProductMappingException.class,
                () -> ProductMapper.toDTO(null));

        assertEquals("Product cannot be null", exception.getMessage());
    }
}
