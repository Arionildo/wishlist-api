package com.ari.wishlist.presentation.controller;

import com.ari.wishlist.application.dto.ProductDTO;
import com.ari.wishlist.application.dto.WishlistDTO;
import com.ari.wishlist.application.mapper.WishlistMapper;
import com.ari.wishlist.application.usecase.*;
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

    @PostMapping("/{customerId}/add-product/{productId}")
    public ResponseEntity<WishlistDTO> addProductToWishlist(@PathVariable String customerId, @PathVariable String productId) {
        var wishlistDTO = WishlistMapper.toDTO(addProductToWishlistUseCase.execute(customerId, productId));
        return ResponseEntity.ok(wishlistDTO);
    }

    @DeleteMapping("/{customerId}/remove-product/{productId}")
    public ResponseEntity<WishlistDTO> removeProductFromWishlist(@PathVariable String customerId, @PathVariable String productId) {
        var wishlistDTO = WishlistMapper.toDTO(removeProductFromWishlistUseCase.execute(customerId, productId));
        return ResponseEntity.ok(wishlistDTO);
    }

    @GetMapping("/{customerId}")
    public ResponseEntity<List<ProductDTO>> getProductsFromWishlist(@PathVariable String customerId) {
        var productDTOList = getAllProductsFromWishlistUseCase.execute(customerId);

        if (productDTOList.isEmpty()) {
            return ResponseEntity.notFound().build();
        }

        return ResponseEntity.ok(productDTOList);
    }

    @GetMapping("/{customerId}/has-product/{productId}")
    public ResponseEntity<Boolean> hasProductInWishlist(@PathVariable String customerId, @PathVariable String productId) {
        var hasProduct = hasProductInWishlistUseCase.execute(customerId, productId);
        return ResponseEntity.ok(hasProduct);
    }

    @DeleteMapping("/{customerId}")
    public ResponseEntity<Void> deleteWishlist(@PathVariable String customerId) {
        deleteWishlistUseCase.execute(customerId);
        return ResponseEntity.noContent().build();
    }
}
