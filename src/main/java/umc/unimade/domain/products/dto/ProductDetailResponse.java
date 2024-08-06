package umc.unimade.domain.products.dto;

import lombok.*;
import java.util.List;
import java.util.stream.Collectors;

import umc.unimade.domain.products.entity.ProductDetailImage;
import umc.unimade.domain.products.entity.Products;

@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ProductDetailResponse {
    private String detail;
    private List<String> detailImages;

    public static ProductDetailResponse from(Products product){
        return ProductDetailResponse.builder()
                .detail(product.getContent())
                .detailImages(product.getProductDetailImages().stream()
                        .map(ProductDetailImage::getImageUrl)
                        .collect(Collectors.toList()))
                .build();
    }
}
