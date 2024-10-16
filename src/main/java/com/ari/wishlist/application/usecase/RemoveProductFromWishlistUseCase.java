package com.ari.wishlist.application.usecase;

import com.ari.wishlist.domain.exception.WishlistNotFoundException;
import com.ari.wishlist.domain.model.Wishlist;
import com.ari.wishlist.domain.repository.WishlistRepository;
import org.springframework.stereotype.Component;

@Component
public class RemoveProductFromWishlistUseCase {

    private final WishlistRepository wishlistRepository;

    public RemoveProductFromWishlistUseCase(WishlistRepository wishlistRepository) {
        this.wishlistRepository = wishlistRepository;
    }

    public Wishlist execute(String customerId, String productId) {
        var wishlist = wishlistRepository.findByCustomerId(customerId)
                .orElseThrow(() -> new WishlistNotFoundException("Wishlist not found"));

        wishlist.removeProduct(productId);
        return wishlistRepository.save(wishlist);
    }
}
