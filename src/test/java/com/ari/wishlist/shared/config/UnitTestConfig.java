package com.ari.wishlist.shared.config;

import com.ari.wishlist.domain.repository.WishlistRepository;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
public abstract class UnitTestConfig {

    @Mock
    protected WishlistRepository wishlistRepository;
}
