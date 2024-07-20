package umc.unimade.domain.favorite.dto;
import lombok.*;
import umc.unimade.domain.favorite.entity.FavoriteSeller;

@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class FavoriteSellerResponse {
    private Long id;
    private Long sellerId;
    private String name;
    private String profileImage;

    public static FavoriteSellerResponse to(FavoriteSeller favoriteSeller){
        return FavoriteSellerResponse.builder()
                .id(favoriteSeller.getId())
                .sellerId(favoriteSeller.getSeller().getId())
                .name(favoriteSeller.getSeller().getName())
                .profileImage(favoriteSeller.getSeller().getProfileImage())
                .build();
    }
}
