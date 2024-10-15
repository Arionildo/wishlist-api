package com.ari.wishlist.application.mapper;

import com.ari.wishlist.application.dto.ProductDTO;
import com.ari.wishlist.domain.model.Product;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class ProductMapperUnitTest {

    @Test
    void toDomain_ShouldConvertProductDTOToProduct() {
        ProductDTO productDTO = ProductDTO.builder()
                .id("product-1")
                .name("Test Product")
                .price(150.0)
                .build();

        Product product = ProductMapper.toDomain(productDTO);

        assertNotNull(product);
        assertEquals(productDTO.getId(), product.getProductId());
        assertEquals(productDTO.getName(), product.getName());
        assertEquals(productDTO.getPrice(), product.getPrice());
    }

    @Test
    void toDTO_ShouldConvertProductToProductDTO() {
        Product product = Product.builder()
                .productId("product-1")
                .name("Test Product")
                .price(150.0)
                .build();

        ProductDTO productDTO = ProductMapper.toDTO(product);

        assertNotNull(productDTO);
        assertEquals(product.getProductId(), productDTO.getId());
        assertEquals(product.getName(), productDTO.getName());
        assertEquals(product.getPrice(), productDTO.getPrice());
    }
}
