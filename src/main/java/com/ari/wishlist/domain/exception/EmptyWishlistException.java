package com.ari.wishlist.domain.exception;

public class EmptyWishlistException extends RuntimeException {
    public EmptyWishlistException(String message) {
        super(message);
    }
}
