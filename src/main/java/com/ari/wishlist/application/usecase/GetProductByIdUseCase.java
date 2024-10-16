package com.ari.wishlist.application.usecase;

import com.ari.wishlist.application.dto.ProductDTO;
import com.ari.wishlist.infrastructure.external.ProductClient;
import com.ari.wishlist.infrastructure.external.exception.ProductNotFoundException;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

@Component
public class GetProductByIdUseCase {
    private final ProductClient productClient;

    public GetProductByIdUseCase(@Qualifier("productClientFallback") ProductClient productClient) {
        this.productClient = productClient;
    }

    public ProductDTO execute(String productId) {
        var productDTO = productClient.getProductById(productId);

        if ("unknown".equals(productDTO.getId())) {
            throw new ProductNotFoundException(productId);
        }

        return productDTO;
    }
}
