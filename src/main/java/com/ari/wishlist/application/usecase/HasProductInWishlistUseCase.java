package com.ari.wishlist.application.usecase;

import com.ari.wishlist.domain.exception.ProductNotInWishlistException;
import com.ari.wishlist.domain.repository.WishlistRepository;
import org.springframework.stereotype.Component;

@Component
public class HasProductInWishlistUseCase {
    private final WishlistRepository wishlistRepository;

    public HasProductInWishlistUseCase(WishlistRepository wishlistRepository) {
        this.wishlistRepository = wishlistRepository;
    }

    public boolean execute(String customerId, String productId) {
        var wishlist = wishlistRepository.findByCustomerId(customerId)
                .orElseThrow(() -> new ProductNotInWishlistException("Wishlist not found for customer ID: " + customerId));

        return wishlist.hasProduct(productId);
    }
}
