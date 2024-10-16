package com.ari.wishlist.domain.model;

import com.ari.wishlist.domain.exception.ProductNotInWishlistException;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Document(collection = "wishlists")
public class Wishlist {

    @Id
    @Builder.Default
    private String id = UUID.randomUUID().toString();
    private String customerId;
    private List<Product> products;

    public void addProduct(Product product) {
        if (products == null) {
            products = new ArrayList<>();
        }
        products.add(product);
    }

    public void removeProduct(String productId) {
        if (products == null || products.isEmpty()) {
            throw new ProductNotInWishlistException("Product not found in wishlist");
        }

        boolean productRemoved = products.removeIf(product -> product.getProductId().equals(productId));

        if (!productRemoved) {
            throw new ProductNotInWishlistException("Product not found in wishlist");
        }
    }

    public boolean hasProduct(String productId) {
        return products.stream()
                .anyMatch(product -> product.getProductId().equals(productId));
    }
}
