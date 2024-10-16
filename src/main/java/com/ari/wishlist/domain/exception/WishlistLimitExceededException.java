package com.ari.wishlist.domain.exception;

public class WishlistLimitExceededException extends RuntimeException {
    public WishlistLimitExceededException(String message) {
        super(message);
    }
}
