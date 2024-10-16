package com.ari.wishlist.domain.repository;

import com.ari.wishlist.domain.model.Wishlist;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface WishlistRepository extends MongoRepository<Wishlist, String> {
    Optional<Wishlist> findByCustomerId(String customerId);
    void deleteByCustomerId(String customerId);
}
