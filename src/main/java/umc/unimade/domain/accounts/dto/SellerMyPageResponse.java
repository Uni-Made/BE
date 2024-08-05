package umc.unimade.domain.accounts.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import umc.unimade.domain.accounts.entity.Seller;
import umc.unimade.domain.products.dto.MyPageProductResponse;

import java.util.List;

@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class SellerMyPageResponse {

    private Long sellerId;
    private String name;
    private String profileImage;
    private String description;
    private Long favoriteCount;
    private List<MyPageProductResponse> sellingProducts;
    private List<MyPageProductResponse> soldoutProducts;

    public static SellerMyPageResponse from(Seller seller, List<MyPageProductResponse> sellingProducts, List<MyPageProductResponse> soldoutProducts, Long favoriteCount) {
        return SellerMyPageResponse.builder()
                .sellerId(seller.getId())
                .name(seller.getName())
                .profileImage(seller.getProfileImage() == null || seller.getProfileImage().isEmpty() ? null : seller.getProfileImage())
                .description(seller.getDescription() == null || seller.getDescription().isEmpty() ? null : seller.getDescription())
                .favoriteCount(favoriteCount)
                .sellingProducts(sellingProducts)
                .soldoutProducts(soldoutProducts)
                .build();
    }
}
