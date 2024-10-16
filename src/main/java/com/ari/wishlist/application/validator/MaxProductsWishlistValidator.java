package com.ari.wishlist.application.validator;

import com.ari.wishlist.domain.exception.ProductAlreadyInWishlistException;
import com.ari.wishlist.domain.exception.WishlistLimitExceededException;
import com.ari.wishlist.domain.model.Product;
import com.ari.wishlist.domain.model.Wishlist;
import com.ari.wishlist.infrastructure.configuration.WishlistConfig;
import org.springframework.stereotype.Component;

@Component
public class MaxProductsWishlistValidator implements WishlistValidator {

    private final WishlistConfig wishlistConfig;

    public MaxProductsWishlistValidator(WishlistConfig wishlistConfig) {
        this.wishlistConfig = wishlistConfig;
    }

    @Override
    public void validate(Wishlist wishlist, Product product) {
        if (wishlist.getProducts() != null && wishlist.getProducts().size() >= wishlistConfig.getMaxProducts()) {
            throw new WishlistLimitExceededException("Wishlist cannot contain more than " + wishlistConfig.getMaxProducts() + " products");
        }
        if (wishlist.getProducts() != null && wishlist.hasProduct(product.getProductId())) {
            throw new ProductAlreadyInWishlistException("Product is already in the wishlist");
        }
    }
}
