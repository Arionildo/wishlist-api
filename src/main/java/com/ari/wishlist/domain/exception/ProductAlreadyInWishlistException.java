package com.ari.wishlist.domain.exception;

public class ProductAlreadyInWishlistException extends RuntimeException {
    public ProductAlreadyInWishlistException(String message) {
        super(message);
    }
}
