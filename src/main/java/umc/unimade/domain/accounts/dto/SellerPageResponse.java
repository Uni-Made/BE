package umc.unimade.domain.accounts.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.domain.Page;
import umc.unimade.domain.accounts.entity.Seller;
import umc.unimade.domain.products.entity.Products;

import java.util.List;

// TODO - md 찜 수, 상품 찜 수, 접속한 buyer가 해당 seller 찜한 여부, + 설명창,인스타 (?)
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class SellerPageResponse {
    private Long sellerId;
    private String profileImage;
    private String name;
    private String description;
    private Page<ProductsResponse> products;

    public static SellerPageResponse of(Seller seller, Page<ProductsResponse> products) {
        return SellerPageResponse.builder()
                .sellerId(seller.getId())
                .profileImage(seller.getProfileImage() == null || seller.getProfileImage().isEmpty() ? null : seller.getProfileImage())
                .name(seller.getName())
                .description(seller.getDescription() == null || seller.getDescription().isEmpty() ? null : seller.getDescription())
                .products(products)
                .build();
    }

    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    public static class ProductsResponse {
        private Long productId;
        private String name;
        private Long price;
        private String imageUrl;

        public static ProductsResponse from(Products product) {
            return ProductsResponse.builder()
                    .productId(product.getId())
                    .name(product.getName())
                    .price(product.getPrice())
                    .imageUrl(product.getProductImages().isEmpty() ? null : product.getProductImages().get(0).getImageUrl())
                    .build();
        }
    }
}
