package com.ari.wishlist.presentation.errorhandling;

import com.ari.wishlist.application.exception.ProductMappingException;
import com.ari.wishlist.application.exception.WishlistMappingException;
import com.ari.wishlist.domain.exception.WishlistNotFoundException;
import com.ari.wishlist.infrastructure.external.exception.ProductNotFoundException;
import com.ari.wishlist.domain.exception.WishlistLimitExceededException;
import com.ari.wishlist.domain.exception.ProductAlreadyInWishlistException;
import com.ari.wishlist.domain.exception.ProductNotInWishlistException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(value = {
            ProductNotFoundException.class,
            WishlistNotFoundException.class
    })
    public ResponseEntity<String> handleNotFoundException(Exception exception) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(exception.getMessage());
    }

    @ExceptionHandler(value = {
            WishlistLimitExceededException.class,
            ProductAlreadyInWishlistException.class,
            ProductNotInWishlistException.class,
            ProductMappingException.class,
            WishlistMappingException.class
    })
    public ResponseEntity<String> handleBadRequestException(Exception exception) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(exception.getMessage());
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<String> handleGeneralException(Exception exception) {
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("An unexpected error occurred: " + exception.getMessage());
    }
}
