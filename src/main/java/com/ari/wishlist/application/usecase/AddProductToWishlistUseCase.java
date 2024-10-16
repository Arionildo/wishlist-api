package com.ari.wishlist.application.usecase;

import com.ari.wishlist.application.mapper.ProductMapper;
import com.ari.wishlist.application.validator.WishlistValidator;
import com.ari.wishlist.domain.model.Wishlist;
import com.ari.wishlist.domain.repository.WishlistRepository;
import org.springframework.stereotype.Component;

@Component
public class AddProductToWishlistUseCase {
    private final WishlistRepository wishlistRepository;
    private final WishlistValidator wishlistValidator;
    private final GetProductByIdUseCase getProductByIdUseCase;

    public AddProductToWishlistUseCase(WishlistRepository wishlistRepository,
                                       WishlistValidator wishlistValidator,
                                       GetProductByIdUseCase getProductByIdUseCase) {
        this.wishlistRepository = wishlistRepository;
        this.wishlistValidator = wishlistValidator;
        this.getProductByIdUseCase = getProductByIdUseCase;
    }

    public Wishlist execute(String customerId, String productId) {
        var productDTO = getProductByIdUseCase.execute(productId);
        var product = ProductMapper.toDomain(productDTO);
        var wishlist = wishlistRepository.findByCustomerId(customerId)
                .orElse(Wishlist.builder().customerId(customerId).build());

        wishlistValidator.validate(wishlist, product);

        wishlist.addProduct(product);
        return wishlistRepository.save(wishlist);
    }
}
