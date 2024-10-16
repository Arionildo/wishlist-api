package com.ari.wishlist.application.usecase;

import com.ari.wishlist.domain.repository.WishlistRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class DeleteWishlistUseCaseTest {

    @Mock
    WishlistRepository wishlistRepository;

    @InjectMocks
    DeleteWishlistUseCase deleteWishlistUseCase;

    @Test
    void givenCustomerId_whenExecute_thenDeletesWishlist() {
        String customerId = "customer-1";

        deleteWishlistUseCase.execute(customerId);

        verify(wishlistRepository).deleteByCustomerId(customerId);
    }
}
