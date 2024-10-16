package com.ari.wishlist.domain.exception;

public class ProductNotInWishlistException extends RuntimeException {
    public ProductNotInWishlistException(String message) {
        super(message);
    }
}
