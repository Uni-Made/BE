package umc.unimade.domain.favorite.dto;

import lombok.*;
import umc.unimade.domain.favorite.entity.FavoriteSeller;

import java.util.List;
import java.util.stream.Collectors;

@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder

public class FavoriteSellersListResponse {
    private List<FavoriteSellerResponse> favoriteSellers;
    private Long nextCursor;
    private Boolean isLast;

    public static FavoriteSellersListResponse from(List<FavoriteSeller> favoriteSellersList, Long nextCursor, Boolean isLast) {
        List<FavoriteSellerResponse> sellers = favoriteSellersList.stream()
                .map(FavoriteSellerResponse::from)
                .collect(Collectors.toList());
        return FavoriteSellersListResponse.builder()
                .favoriteSellers(sellers)
                .nextCursor(nextCursor)
                .isLast(isLast)
                .build();
    }
}
