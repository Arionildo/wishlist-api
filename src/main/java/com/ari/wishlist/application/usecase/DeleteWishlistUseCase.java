package com.ari.wishlist.application.usecase;

import com.ari.wishlist.domain.exception.WishlistNotFoundException;
import com.ari.wishlist.domain.repository.WishlistRepository;
import org.springframework.stereotype.Component;

@Component
public class DeleteWishlistUseCase {
    private final WishlistRepository wishlistRepository;

    public DeleteWishlistUseCase(WishlistRepository wishlistRepository) {
        this.wishlistRepository = wishlistRepository;
    }

    public void execute(String customerId) {
        boolean exists = wishlistRepository.existsByCustomerId(customerId);
        if (!exists) {
            throw new WishlistNotFoundException("Wishlist not found for customer ID: " + customerId);
        }
        wishlistRepository.deleteByCustomerId(customerId);
    }
}
