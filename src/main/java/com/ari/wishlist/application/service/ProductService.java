package com.ari.wishlist.application.service;

import com.ari.wishlist.application.dto.ProductDTO;
import com.ari.wishlist.infrastructure.external.ProductClient;
import com.ari.wishlist.infrastructure.external.exception.ProductNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class ProductService {

    private final ProductClient productClient;

    public ProductService(ProductClient productClient) {
        this.productClient = productClient;
    }

    public ProductDTO getProductById(String productId) {
        ProductDTO product = productClient.getProductById(productId);

        if ("unknown".equals(product.getId())) {
            throw new ProductNotFoundException(productId);
        }

        return product;
    }
}
