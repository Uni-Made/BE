package umc.unimade.domain.accounts.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import umc.unimade.domain.accounts.entity.Seller;
import umc.unimade.domain.products.dto.MyPageProductResponse;
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
    private List<MyPageProductResponse> sellingProducts;
    private List<MyPageProductResponse> soldoutProducts;

    public static SellerMyPageResponse from(Seller seller, List<MyPageProductResponse> sellingProducts, List<MyPageProductResponse> soldoutProducts) {
        return SellerMyPageResponse.builder()
                .sellerId(seller.getId())
                .name(seller.getName())
                .profileImage(seller.getProfileImage().isEmpty() ? null : seller.getProfileImage())
                .sellingProducts(sellingProducts)
                .soldoutProducts(soldoutProducts)
                .build();
    }
}
