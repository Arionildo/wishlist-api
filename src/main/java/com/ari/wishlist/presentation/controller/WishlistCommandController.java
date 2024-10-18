package com.ari.wishlist.presentation.controller;

import com.ari.wishlist.application.dto.ResponseDTO;
import com.ari.wishlist.application.dto.WishlistDTO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/wishlist")
public interface WishlistCommandController {

    @Operation(summary = "Add a product to the wishlist", description = "Adds a specified product to the user's wishlist.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Product added successfully."),
            @ApiResponse(responseCode = "400", description = "Bad request due to business rules."),
            @ApiResponse(responseCode = "404", description = "Product not found."),
            @ApiResponse(responseCode = "500", description = "Internal server error.")
    })
    @PostMapping("/{customerId}/add-product/{productId}")
    ResponseEntity<ResponseDTO<WishlistDTO>> addProductToWishlist(@PathVariable String customerId, @PathVariable String productId);

    @Operation(summary = "Remove a product from the wishlist", description = "Removes a specified product from the user's wishlist.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Product removed successfully."),
            @ApiResponse(responseCode = "400", description = "Bad request due to business rules."),
            @ApiResponse(responseCode = "404", description = "Product not found or wishlist not found."),
            @ApiResponse(responseCode = "500", description = "Internal server error.")
    })
    @DeleteMapping("/{customerId}/remove-product/{productId}")
    ResponseEntity<ResponseDTO<WishlistDTO>> removeProductFromWishlist(@PathVariable String customerId, @PathVariable String productId);

    @Operation(summary = "Delete the wishlist", description = "Deletes the user's wishlist.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Wishlist deleted successfully."),
            @ApiResponse(responseCode = "404", description = "Wishlist not found."),
            @ApiResponse(responseCode = "500", description = "Internal server error.")
    })
    @DeleteMapping("/{customerId}")
    ResponseEntity<ResponseDTO<Void>> deleteWishlist(@PathVariable String customerId);
}
