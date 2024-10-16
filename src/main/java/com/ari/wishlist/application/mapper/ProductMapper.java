package com.ari.wishlist.application.mapper;

import com.ari.wishlist.application.dto.ProductDTO;
import com.ari.wishlist.domain.model.Product;
import com.ari.wishlist.application.exception.ProductMappingException;

public class ProductMapper {

    private ProductMapper() {
    }

    public static Product toDomain(ProductDTO productDTO) {
        if (productDTO == null) {
            throw new ProductMappingException("ProductDTO cannot be null");
        }
        return Product.builder()
                .productId(productDTO.getId())
                .name(productDTO.getName())
                .price(productDTO.getPrice())
                .build();
    }

    public static ProductDTO toDTO(Product product) {
        if (product == null) {
            throw new ProductMappingException("Product cannot be null");
        }
        return ProductDTO.builder()
                .id(product.getProductId())
                .name(product.getName())
                .price(product.getPrice())
                .build();
    }
}
