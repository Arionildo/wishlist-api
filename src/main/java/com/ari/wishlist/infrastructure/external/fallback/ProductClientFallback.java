package com.ari.wishlist.infrastructure.external.fallback;

import com.ari.wishlist.application.dto.ProductDTO;
import com.ari.wishlist.infrastructure.external.ProductClient;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;
import java.util.stream.IntStream;

@Component
public class ProductClientFallback implements ProductClient {

    private final List<ProductDTO> products;

    public ProductClientFallback() {
        products = IntStream.rangeClosed(1, 30)
                .mapToObj(i -> ProductDTO.builder()
                        .id("product-" + i)
                        .name("Product " + i)
                        .price(BigDecimal.valueOf(100.0 + i * 2))
                        .build())
                .toList();
    }

    @Override
    public ProductDTO getProductById(String productId) {
        Optional<ProductDTO> product = products.stream()
                .filter(p -> p.getId().equals(productId))
                .findFirst();

        return product.orElse(ProductDTO.builder()
                .id("unknown")
                .build());
    }
}
