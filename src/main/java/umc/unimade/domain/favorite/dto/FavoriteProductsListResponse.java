package umc.unimade.domain.favorite.dto;

import lombok.*;
import umc.unimade.domain.favorite.entity.FavoriteProduct;

import java.util.List;
import java.util.stream.Collectors;

@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class FavoriteProductsListResponse {
    private List<FavoriteProductResponse> favoriteProducts;
    private Long nextCursor;
    private Boolean isLast;

    public static FavoriteProductsListResponse from(List<FavoriteProduct> favoriteProductsList, Long nextCursor, Boolean isLast) {
        List<FavoriteProductResponse> products = favoriteProductsList.stream()
                .map(FavoriteProductResponse::from)
                .collect(Collectors.toList());
        return FavoriteProductsListResponse.builder()
                .favoriteProducts(products)
                .nextCursor(nextCursor)
                .isLast(isLast)
                .build();
    }
}
