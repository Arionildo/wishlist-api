package com.ari.wishlist.presentation.controller.impl;

import com.ari.wishlist.application.dto.ResponseDTO;
import com.ari.wishlist.application.dto.WishlistDTO;
import com.ari.wishlist.application.mapper.WishlistMapper;
import com.ari.wishlist.application.usecase.AddProductToWishlistUseCase;
import com.ari.wishlist.application.usecase.RemoveProductFromWishlistUseCase;
import com.ari.wishlist.application.usecase.DeleteWishlistUseCase;
import com.ari.wishlist.presentation.controller.WishlistCommandController;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class WishlistCommandControllerImpl implements WishlistCommandController {

    private final AddProductToWishlistUseCase addProductToWishlistUseCase;
    private final RemoveProductFromWishlistUseCase removeProductFromWishlistUseCase;
    private final DeleteWishlistUseCase deleteWishlistUseCase;

    @Override
    public ResponseEntity<ResponseDTO<WishlistDTO>> addProductToWishlist(String customerId, String productId) {
        var wishlistDTO = WishlistMapper.toDTO(addProductToWishlistUseCase.execute(customerId, productId));
        return ResponseEntity.ok(ResponseDTO.<WishlistDTO>builder()
                .message("Product added successfully")
                .data(wishlistDTO)
                .build());
    }

    @Override
    public ResponseEntity<ResponseDTO<WishlistDTO>> removeProductFromWishlist(String customerId, String productId) {
        var wishlistDTO = WishlistMapper.toDTO(removeProductFromWishlistUseCase.execute(customerId, productId));
        return ResponseEntity.ok(ResponseDTO.<WishlistDTO>builder()
                .message("Product removed successfully")
                .data(wishlistDTO)
                .build());
    }

    @Override
    public ResponseEntity<ResponseDTO<Void>> deleteWishlist(String customerId) {
        deleteWishlistUseCase.execute(customerId);
        return ResponseEntity.noContent().build();
    }
}
