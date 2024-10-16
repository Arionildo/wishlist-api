package com.ari.wishlist.infrastructure.configuration;

import lombok.Getter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

@Getter
@Configuration
public class WishlistConfig {

    @Value("${wishlist.max-products}")
    private int maxProducts;

}
