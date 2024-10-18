package com.ari.wishlist.application.usecase;

import com.ari.wishlist.domain.exception.WishlistNotFoundException;
import com.ari.wishlist.domain.repository.WishlistRepository;
import com.ari.wishlist.shared.config.UnitTestConfig;
import com.ari.wishlist.shared.data.UnitTestData;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

class DeleteWishlistUseCaseTest extends UnitTestConfig {

    @InjectMocks
    DeleteWishlistUseCase deleteWishlistUseCase;

    @Test
    void givenExistingWishlist_whenExecute_thenDeletesWishlist() {
        String customerId = UnitTestData.CUSTOMER_ID_1;

        when(wishlistRepository.existsByCustomerId(customerId)).thenReturn(true);

        deleteWishlistUseCase.execute(customerId);

        verify(wishlistRepository).deleteByCustomerId(customerId);
    }

    @Test
    void givenNonExistentWishlist_whenExecute_thenThrowsWishlistNotFoundException() {
        String customerId = UnitTestData.CUSTOMER_ID_1;

        when(wishlistRepository.existsByCustomerId(customerId)).thenReturn(false);

        assertThrows(WishlistNotFoundException.class, () -> deleteWishlistUseCase.execute(customerId));

        verify(wishlistRepository, never()).deleteByCustomerId(customerId);
    }
}
