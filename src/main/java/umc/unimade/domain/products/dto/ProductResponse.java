package umc.unimade.domain.products.dto;



import lombok.*;
import java.util.List;
import umc.unimade.domain.products.entity.Products;
import umc.unimade.domain.products.entity.ProductsImage;

import java.time.LocalDate;
import java.util.stream.Collectors;

@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ProductResponse {
    private Long productId;
    private Long sellerId;
    private String sellerName;
    private String university;
    private String productName;
    private LocalDate deadline;
    private Long price;
    private List<String> productImages;
    private List<OptionResponse> options;
    private String detail;
    private List<String> reviews;
    private List<String> questions;

    public static ProductResponse to(Products product) {
        return ProductResponse.builder()
                .productId(product.getId())
                .sellerId(product.getSeller().getId())
                .sellerName(product.getSeller().getName())
                .university(product.getUniversity())
                .productName(product.getName())
                .deadline(product.getDeadline())
                .price(product.getPrice())
                .productImages(product.getProductImages().stream().map(ProductsImage::getImageUrl).collect(Collectors.toList()))
                .options(product.getOptions().stream()
                        .map(OptionResponse::to)
                        .collect(Collectors.toList()))
                .build();
    }
    public void setDetail(String detail) {
        this.detail = detail;
    }

    public void setReviews(List<String> reviews) {
        this.reviews = reviews;
    }

    public void setQuestions(List<String> questions) {
        this.questions = questions;
    }
}


