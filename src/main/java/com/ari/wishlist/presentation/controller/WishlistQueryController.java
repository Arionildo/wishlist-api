package com.ari.wishlist.presentation.controller;

import com.ari.wishlist.application.dto.ProductDTO;
import com.ari.wishlist.application.dto.ResponseDTO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/v1/wishlist")
public interface WishlistQueryController {

    @Operation(summary = "Get all products from the wishlist", description = "Retrieves all products in the user's wishlist.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully retrieved products."),
            @ApiResponse(responseCode = "404", description = "Wishlist not found."),
            @ApiResponse(responseCode = "500", description = "Internal server error.")
    })
    @GetMapping("/{customerId}")
    ResponseEntity<ResponseDTO<List<ProductDTO>>> getProductsFromWishlist(@PathVariable String customerId);

    @Operation(summary = "Check if product is in wishlist", description = "Checks if a specified product is in the user's wishlist.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Product existence checked successfully."),
            @ApiResponse(responseCode = "404", description = "Wishlist or product not found."),
            @ApiResponse(responseCode = "500", description = "Internal server error.")
    })
    @GetMapping("/{customerId}/has-product/{productId}")
    ResponseEntity<ResponseDTO<Boolean>> hasProductInWishlist(@PathVariable String customerId, @PathVariable String productId);
}
