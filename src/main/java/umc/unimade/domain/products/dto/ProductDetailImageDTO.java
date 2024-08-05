package umc.unimade.domain.products.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import umc.unimade.domain.products.entity.ProductDetailImage;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ProductDetailImageDTO {

    private Long id;
    private String url;

    public static ProductDetailImageDTO from(ProductDetailImage productDetailImage) {
        return ProductDetailImageDTO.builder()
                .id(productDetailImage.getId())
                .url(productDetailImage.getImageUrl())
                .build();
    }
}