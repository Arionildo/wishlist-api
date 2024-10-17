package com.ari.wishlist.presentation.errorhandling;

import com.ari.wishlist.application.dto.ResponseDTO;
import com.ari.wishlist.application.exception.ProductMappingException;
import com.ari.wishlist.application.exception.WishlistMappingException;
import com.ari.wishlist.domain.exception.*;
import com.ari.wishlist.infrastructure.external.exception.ProductNotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(value = {
            ProductNotFoundException.class,
            WishlistNotFoundException.class,
            UsernameNotFoundException.class,
            EmptyWishlistException.class
    })
    public ResponseEntity<ResponseDTO<String>> handleNotFoundException(Exception exception) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(
                ResponseDTO.<String>builder()
                        .message(exception.getMessage())
                        .data(null)
                        .build()
        );
    }

    @ExceptionHandler(value = {
            WishlistLimitExceededException.class,
            ProductAlreadyInWishlistException.class,
            ProductNotInWishlistException.class,
            ProductMappingException.class,
            WishlistMappingException.class
    })
    public ResponseEntity<ResponseDTO<String>> handleBadRequestException(Exception exception) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(
                ResponseDTO.<String>builder()
                        .message(exception.getMessage())
                        .data(null)
                        .build()
        );
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ResponseDTO<String>> handleGeneralException(Exception exception) {
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(
                ResponseDTO.<String>builder()
                        .message("An unexpected error occurred: " + exception.getMessage())
                        .data(null)
                        .build()
        );
    }
}
