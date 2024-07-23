package umc.unimade.domain.products.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import umc.unimade.domain.products.entity.Products;

@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class SoldoutProductResponse {

    private Long productId;
    private String imageUrl;
    private String name;
    private Long price;

    public static SoldoutProductResponse from(Products product) {
        return SoldoutProductResponse.builder()
                .productId(product.getId())
                .imageUrl(product.getProductImages() == null || product.getProductImages().isEmpty() || product.getProductImages().get(0).getImageUrl() == null || product.getProductImages().get(0).getImageUrl().isEmpty() ? null : product.getProductImages().get(0).getImageUrl())
                .name(product.getName())
                .price(product.getPrice())
                .build();
    }
}
