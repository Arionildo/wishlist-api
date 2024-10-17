package com.ari.wishlist.application.usecase;

import com.ari.wishlist.application.dto.ProductDTO;
import com.ari.wishlist.application.mapper.ProductMapper;
import com.ari.wishlist.domain.exception.EmptyWishlistException;
import com.ari.wishlist.domain.exception.WishlistNotFoundException;
import com.ari.wishlist.domain.repository.WishlistRepository;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class GetAllProductsFromWishlistUseCase {
    private final WishlistRepository wishlistRepository;

    public GetAllProductsFromWishlistUseCase(WishlistRepository wishlistRepository) {
        this.wishlistRepository = wishlistRepository;
    }

    public List<ProductDTO> execute(String customerId) {
        var wishlist = wishlistRepository.findByCustomerId(customerId)
                .orElseThrow(() -> new WishlistNotFoundException("Wishlist not found for customer ID: " + customerId));

        var products = wishlist.getProducts();
        if (products.isEmpty()) {
            throw new EmptyWishlistException("Wishlist is empty for customer ID: " + customerId);
        }

        return products.stream()
                .map(ProductMapper::toDTO)
                .toList();
    }
}
