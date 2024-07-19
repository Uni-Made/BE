package umc.unimade.domain.favorite.dto;

import lombok.*;
import umc.unimade.domain.favorite.entity.FavoriteProduct;
import umc.unimade.domain.products.entity.Products;

@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class FavoriteProductResponse {
    private Long id;
    private Long productId;
    private String name;
    private Long price;
    private String productImage;
    private Long sellerId;
    private String sellerName;


    public static FavoriteProductResponse to(FavoriteProduct favoriteProduct){
        Products product = favoriteProduct.getProduct();
        return FavoriteProductResponse.builder()
                .id(favoriteProduct.getId())
                .productId(product.getId())
                .name(product.getName())
                .price(product.getPrice())
                .productImage(product.getProductImages().isEmpty() ? null : product.getProductImages().get(0).getImageUrl())
                .sellerId(product.getSeller().getId())
                .sellerName(product.getSeller().getName())
                .build();
    }
}
