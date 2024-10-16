package com.ari.wishlist.application.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class WishlistDTO {
    private String id;
    private String customerId;
    private List<ProductDTO> products;
}
