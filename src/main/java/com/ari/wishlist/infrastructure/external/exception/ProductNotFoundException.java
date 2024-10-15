package com.ari.wishlist.infrastructure.external.exception;

public class ProductNotFoundException extends RuntimeException {

    public ProductNotFoundException(String productId) {
        super("Product with ID " + productId + " not found.");
    }
}
