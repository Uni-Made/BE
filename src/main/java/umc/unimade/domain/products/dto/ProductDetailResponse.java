package umc.unimade.domain.products.dto;

import lombok.*;
import umc.unimade.domain.products.entity.Products;

@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ProductDetailResponse {
    private String detail;

    public static ProductDetailResponse to(Products product){
        return ProductDetailResponse.builder()
                .detail(product.getContent())
                .build();
    }
}
