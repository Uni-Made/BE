package umc.unimade.domain.products.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import umc.unimade.domain.products.entity.ProductsImage;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ProductsImageDTO {

    private Long id;
    private String url;

    public static ProductsImageDTO from(ProductsImage productsImage) {
        return ProductsImageDTO.builder()
                .id(productsImage.getId())
                .url(productsImage.getImageUrl())
                .build();
    }
}