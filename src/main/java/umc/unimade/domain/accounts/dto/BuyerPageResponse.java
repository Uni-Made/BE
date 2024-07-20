package umc.unimade.domain.accounts.dto;

import lombok.*;
import umc.unimade.domain.accounts.entity.Buyer;
import umc.unimade.domain.favorite.dto.FavoriteProductResponse;
import umc.unimade.domain.favorite.dto.FavoriteSellerResponse;
import umc.unimade.domain.favorite.entity.FavoriteProduct;
import umc.unimade.domain.favorite.entity.FavoriteSeller;

import java.util.List;

@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class BuyerPageResponse {
    private Long buyerId;
    private String name;
    private String profileImg;
    private List<FavoriteProductResponse> favoriteProducts;
    private List<FavoriteSellerResponse> favoriteSellers;

    public static BuyerPageResponse from(Buyer buyer,List<FavoriteProductResponse> favoriteProducts, List<FavoriteSellerResponse> favoriteSellers) {
        return BuyerPageResponse.builder()
                .buyerId(buyer.getId())
                .name(buyer.getName())
                .profileImg(buyer.getProfileImage())
                .favoriteProducts(favoriteProducts)
                .favoriteSellers(favoriteSellers)
                .build();
    }
}
