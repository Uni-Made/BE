package umc.unimade.domain.products.dto;

import lombok.*;
import umc.unimade.domain.products.entity.Products;

import java.util.List;
import java.util.stream.Collectors;

@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ProductsListResponse {
    private List<ProductInfo> productsList;
    private Long nextCursor;
    private Boolean isLast;

    @Getter
    @AllArgsConstructor
    @NoArgsConstructor
    @Builder
    public static class ProductInfo {
        private Long productId;
        private String name;
        private Long price;
        private String imgUrl;
        private Long sellerId;
        private String sellerName;

        public static ProductInfo from(Products product) {
            return ProductInfo.builder()
                    .productId(product.getId())
                    .name(product.getName())
                    .price(product.getPrice())
                    .imgUrl(product.getProductImages().get(0).getImageUrl())
                    .sellerId(product.getSeller().getId())
                    .sellerName(product.getSeller().getName())
                    .build();
        }
    }

    public static ProductsListResponse from(List<Products> productsList, Long nextCursor, Boolean isLast) {
        List<ProductInfo> products = productsList.stream()
                .map(ProductInfo::from)
                .collect(Collectors.toList());
        return ProductsListResponse.builder()
                .productsList(products)
                .nextCursor(nextCursor)
                .isLast(isLast)
                .build();
    }
}
