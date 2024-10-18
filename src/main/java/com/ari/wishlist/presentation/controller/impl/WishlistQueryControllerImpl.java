package com.ari.wishlist.presentation.controller.impl;

import com.ari.wishlist.application.dto.ProductDTO;
import com.ari.wishlist.application.dto.ResponseDTO;
import com.ari.wishlist.application.usecase.GetAllProductsFromWishlistUseCase;
import com.ari.wishlist.application.usecase.HasProductInWishlistUseCase;
import com.ari.wishlist.presentation.controller.WishlistQueryController;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class WishlistQueryControllerImpl implements WishlistQueryController {

    private final GetAllProductsFromWishlistUseCase getAllProductsFromWishlistUseCase;
    private final HasProductInWishlistUseCase hasProductInWishlistUseCase;

    @Override
    public ResponseEntity<ResponseDTO<List<ProductDTO>>> getProductsFromWishlist(String customerId) {
        var productDTOList = getAllProductsFromWishlistUseCase.execute(customerId);
        return ResponseEntity.ok(ResponseDTO.<List<ProductDTO>>builder()
                .message("Products retrieved successfully")
                .data(productDTOList)
                .build());
    }

    @Override
    public ResponseEntity<ResponseDTO<Boolean>> hasProductInWishlist(String customerId, String productId) {
        var hasProduct = hasProductInWishlistUseCase.execute(customerId, productId);
        var message = hasProduct ? "Product is in the customer's wishlist" : "Product is not in the customer's wishlist";
        return ResponseEntity.ok(ResponseDTO.<Boolean>builder()
                .message(message)
                .data(hasProduct)
                .build());
    }
}
