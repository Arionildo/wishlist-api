package com.ari.wishlist.application.mapper;

import com.ari.wishlist.application.dto.WishlistDTO;
import com.ari.wishlist.domain.model.Wishlist;
import com.ari.wishlist.application.exception.WishlistMappingException;

import java.util.Collections;

public class WishlistMapper {

    private WishlistMapper() {
    }

    public static WishlistDTO toDTO(Wishlist wishlist) {
        if (wishlist == null) {
            throw new WishlistMappingException("Wishlist cannot be null");
        }

        return WishlistDTO.builder()
                .id(wishlist.getId())
                .customerId(wishlist.getCustomerId())
                .products(
                        wishlist.getProducts() != null
                                ? wishlist.getProducts().stream()
                                .map(ProductMapper::toDTO)
                                .toList()
                                : Collections.emptyList()
                )
                .build();
    }
}
