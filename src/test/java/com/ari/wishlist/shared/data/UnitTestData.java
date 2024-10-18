package com.ari.wishlist.shared.data;

import com.ari.wishlist.application.dto.ProductDTO;
import com.ari.wishlist.domain.model.Product;
import com.ari.wishlist.domain.model.Wishlist;

import java.math.BigDecimal;
import java.util.Collections;
import java.util.List;

public class UnitTestData {

    public static final String CUSTOMER_ID_1 = "customer-1";
    public static final String CUSTOMER_ID_2 = "customer-2";
    public static final String CUSTOMER_ID_3 = "customer-3";
    public static final String PRODUCT_ID_1 = "product-1";
    public static final String PRODUCT_ID_2 = "product-2";
    public static final String PRODUCT_ID_3 = "product-3";
    public static final String PRODUCT_ID_21 = "product-21";
    public static final String INVALID_CUSTOMER_ID = "invalid-customer";
    public static final String INVALID_PRODUCT_ID = "invalid-product";

    public static final String PRODUCT_NOT_FOUND_MESSAGE = "Product not found";
    public static final String PRODUCT_NOT_FOUND_IN_WISHLIST_MESSAGE = "Product not found in wishlist";
    public static final String ALREADY_IN_WISHLIST_MESSAGE = "Product is already in the wishlist";
    public static final String WISHLIST_FULL_MESSAGE = "Wishlist cannot contain more than 20 products";
    public static final String WISHLIST_NOT_FOUND_MESSAGE = "Wishlist not found";
    public static final String WISHLIST_NOT_FOUND_CUSTOMER_MESSAGE = "Wishlist not found for customer ID: ";

    public static ProductDTO createProductDTO(String id, String name, BigDecimal price) {
        return ProductDTO.builder()
                .id(id)
                .name(name)
                .price(price)
                .build();
    }

    public static Product createProduct(String productId, String name, BigDecimal price) {
        return Product.builder()
                .productId(productId)
                .name(name)
                .price(price)
                .build();
    }

    public static Wishlist createWishlist(String customerId, List<Product> products) {
        return Wishlist.builder()
                .customerId(customerId)
                .products(products)
                .build();
    }

    public static Wishlist createEmptyWishlist(String customerId) {
        return createWishlist(customerId, Collections.emptyList());
    }
}
