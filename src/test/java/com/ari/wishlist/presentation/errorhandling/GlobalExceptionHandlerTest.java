package com.ari.wishlist.presentation.errorhandling;

import com.ari.wishlist.application.dto.ResponseDTO;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.Objects;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

class GlobalExceptionHandlerTest {

    private final GlobalExceptionHandler globalExceptionHandler = new GlobalExceptionHandler();

    @Test
    void handleGeneralException_shouldReturnInternalServerError() {
        Exception exception = new RuntimeException("Simulated error");

        ResponseEntity<ResponseDTO<String>> response = globalExceptionHandler.handleGeneralException(exception);

        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
        assertEquals("An unexpected error occurred: Simulated error", Objects.requireNonNull(response.getBody()).getMessage());
        assertNull(response.getBody().getData());
    }
}
