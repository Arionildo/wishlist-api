package com.ari.wishlist.application.usecase;

import com.ari.wishlist.domain.repository.WishlistRepository;
import org.springframework.stereotype.Component;

@Component
public class DeleteWishlistUseCase {
    private final WishlistRepository wishlistRepository;

    public DeleteWishlistUseCase(WishlistRepository wishlistRepository) {
        this.wishlistRepository = wishlistRepository;
    }

    public void execute(String customerId) {
        wishlistRepository.deleteByCustomerId(customerId);
    }
}
