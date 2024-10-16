package com.ari.wishlist.application.validator;

import com.ari.wishlist.domain.model.Product;
import com.ari.wishlist.domain.model.Wishlist;

public interface WishlistValidator {
    void validate(Wishlist wishlist, Product product);
}
