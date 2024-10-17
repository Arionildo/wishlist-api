package com.ari.wishlist.presentation.controller;

import com.ari.wishlist.application.dto.ProductDTO;
import com.ari.wishlist.application.dto.ResponseDTO;
import com.ari.wishlist.application.dto.WishlistDTO;
import com.ari.wishlist.application.mapper.WishlistMapper;
import com.ari.wishlist.application.usecase.*;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("api/v1/wishlist")
public class WishlistController {

    private final AddProductToWishlistUseCase addProductToWishlistUseCase;
    private final RemoveProductFromWishlistUseCase removeProductFromWishlistUseCase;
    private final GetAllProductsFromWishlistUseCase getAllProductsFromWishlistUseCase;
    private final HasProductInWishlistUseCase hasProductInWishlistUseCase;
    private final DeleteWishlistUseCase deleteWishlistUseCase;

    public WishlistController(AddProductToWishlistUseCase addProductToWishlistUseCase,
                              RemoveProductFromWishlistUseCase removeProductFromWishlistUseCase,
                              GetAllProductsFromWishlistUseCase getAllProductsFromWishlistUseCase,
                              HasProductInWishlistUseCase hasProductInWishlistUseCase,
                              DeleteWishlistUseCase deleteWishlistUseCase) {
        this.addProductToWishlistUseCase = addProductToWishlistUseCase;
        this.removeProductFromWishlistUseCase = removeProductFromWishlistUseCase;
        this.getAllProductsFromWishlistUseCase = getAllProductsFromWishlistUseCase;
        this.hasProductInWishlistUseCase = hasProductInWishlistUseCase;
        this.deleteWishlistUseCase = deleteWishlistUseCase;
    }

    @Operation(summary = "Add a product to the wishlist",
            description = "Adds a specified product to the user's wishlist.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Product added successfully."),
            @ApiResponse(responseCode = "400", description = "Bad request due to business rules."),
            @ApiResponse(responseCode = "404", description = "Product not found or wishlist not found."),
            @ApiResponse(responseCode = "500", description = "Internal server error.")
    })
    @PostMapping("/{customerId}/add-product/{productId}")
    public ResponseEntity<ResponseDTO<WishlistDTO>> addProductToWishlist(@PathVariable String customerId, @PathVariable String productId) {
        try {
            var wishlistDTO = WishlistMapper.toDTO(addProductToWishlistUseCase.execute(customerId, productId));
            return ResponseEntity.ok(ResponseDTO.<WishlistDTO>builder()
                    .message("Product added successfully")
                    .data(wishlistDTO)
                    .build());
        } catch (Exception exception) {
            return ResponseEntity.badRequest().body(ResponseDTO.<WishlistDTO>builder()
                    .message(exception.getMessage())
                    .data(null)
                    .build());
        }
    }

    @Operation(summary = "Remove a product from the wishlist",
            description = "Removes a specified product from the user's wishlist.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Product removed successfully."),
            @ApiResponse(responseCode = "400", description = "Bad request due to business rules."),
            @ApiResponse(responseCode = "404", description = "Product not found or wishlist not found."),
            @ApiResponse(responseCode = "500", description = "Internal server error.")
    })
    @DeleteMapping("/{customerId}/remove-product/{productId}")
    public ResponseEntity<ResponseDTO<WishlistDTO>> removeProductFromWishlist(@PathVariable String customerId, @PathVariable String productId) {
        try {
            var wishlistDTO = WishlistMapper.toDTO(removeProductFromWishlistUseCase.execute(customerId, productId));
            return ResponseEntity.ok(ResponseDTO.<WishlistDTO>builder()
                    .message("Product removed successfully")
                    .data(wishlistDTO)
                    .build());
        } catch (Exception exception) {
            return ResponseEntity.badRequest().body(ResponseDTO.<WishlistDTO>builder()
                    .message(exception.getMessage())
                    .data(null)
                    .build());
        }
    }

    @Operation(summary = "Get all products from the wishlist",
            description = "Retrieves all products in the user's wishlist.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully retrieved products."),
            @ApiResponse(responseCode = "404", description = "Wishlist not found."),
            @ApiResponse(responseCode = "500", description = "Internal server error.")
    })
    @GetMapping("/{customerId}")
    public ResponseEntity<ResponseDTO<List<ProductDTO>>> getProductsFromWishlist(@PathVariable String customerId) {
        var productDTOList = getAllProductsFromWishlistUseCase.execute(customerId);

        if (productDTOList.isEmpty()) {
            return ResponseEntity.notFound().build();
        }

        return ResponseEntity.ok(ResponseDTO.<List<ProductDTO>>builder()
                .message("Products retrieved successfully")
                .data(productDTOList)
                .build());
    }

    @Operation(summary = "Check if product is in wishlist",
            description = "Checks if a specified product is in the user's wishlist.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Product existence checked successfully."),
            @ApiResponse(responseCode = "404", description = "Wishlist or product not found."),
            @ApiResponse(responseCode = "500", description = "Internal server error.")
    })
    @GetMapping("/{customerId}/has-product/{productId}")
    public ResponseEntity<ResponseDTO<Boolean>> hasProductInWishlist(@PathVariable String customerId, @PathVariable String productId) {
        var hasProduct = hasProductInWishlistUseCase.execute(customerId, productId);
        return ResponseEntity.ok(ResponseDTO.<Boolean>builder()
                .message("Product is in the customer wishlist")
                .data(hasProduct)
                .build());
    }

    @Operation(summary = "Delete the wishlist",
            description = "Deletes the user's wishlist.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Wishlist deleted successfully."),
            @ApiResponse(responseCode = "404", description = "Wishlist not found."),
            @ApiResponse(responseCode = "500", description = "Internal server error.")
    })
    @DeleteMapping("/{customerId}")
    public ResponseEntity<ResponseDTO<Void>> deleteWishlist(@PathVariable String customerId) {
        deleteWishlistUseCase.execute(customerId);
        return ResponseEntity.noContent().build();
    }
}
