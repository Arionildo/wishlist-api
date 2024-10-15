package com.ari.wishlist.application.mapper;

import com.ari.wishlist.application.dto.ProductDTO;
import com.ari.wishlist.domain.model.Product;

public class ProductMapper {

    private ProductMapper() {
    }

    public static Product toDomain(ProductDTO productDTO) {
        return Product.builder()
                .productId(productDTO.getId())
                .name(productDTO.getName())
                .price(productDTO.getPrice())
                .build();
    }

    public static ProductDTO toDTO(Product product) {
        return ProductDTO.builder()
                .id(product.getProductId())
                .name(product.getName())
                .price(product.getPrice())
                .build();
    }
}
