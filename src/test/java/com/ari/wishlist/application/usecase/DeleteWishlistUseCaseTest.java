package com.ari.wishlist.application.usecase;

import com.ari.wishlist.domain.exception.WishlistNotFoundException;
import com.ari.wishlist.domain.repository.WishlistRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class DeleteWishlistUseCaseTest {

    @Mock
    WishlistRepository wishlistRepository;

    @InjectMocks
    DeleteWishlistUseCase deleteWishlistUseCase;

    @Test
    void givenExistingWishlist_whenExecute_thenDeletesWishlist() {
        String customerId = "customer-1";

        when(wishlistRepository.existsByCustomerId(customerId)).thenReturn(true);

        deleteWishlistUseCase.execute(customerId);

        verify(wishlistRepository).deleteByCustomerId(customerId);
    }

    @Test
    void givenNonExistentWishlist_whenExecute_thenThrowsWishlistNotFoundException() {
        String customerId = "customer-1";

        when(wishlistRepository.existsByCustomerId(customerId)).thenReturn(false);

        assertThrows(WishlistNotFoundException.class, () -> deleteWishlistUseCase.execute(customerId));

        verify(wishlistRepository, never()).deleteByCustomerId(customerId);
    }
}
