package umc.unimade.domain.products.dto;



import lombok.*;
import java.util.List;

import umc.unimade.domain.products.entity.Products;
import umc.unimade.domain.products.entity.ProductsImage;
import umc.unimade.domain.qna.dto.QnAListResponse;
import umc.unimade.domain.review.dto.ReviewListResponse;

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
    private Long categoryId;
    private String categoryName;
    private LocalDate deadline;
    private Long price;
    private List<String> productImages;
    private Integer favoriteCount;
    private List<OptionResponse> options;
    private String detail;
    private ReviewListResponse reviews;
    private QnAListResponse questions;

    public static ProductResponse from(Products product) {
        return ProductResponse.builder()
                .productId(product.getId())
                .sellerId(product.getSeller().getId())
                .sellerName(product.getSeller().getName())
                .university(product.getUniversity())
                .productName(product.getName())
                .categoryId(product.getCategory().getId())
                .categoryName(product.getCategory().getName())
                .deadline(product.getDeadline())
                .price(product.getPrice())
                .productImages(product.getProductImages().stream().map(ProductsImage::getImageUrl).collect(Collectors.toList()))
                .favoriteCount(product.getFavoriteProducts().size())
                .options(product.getOptionCategories().stream()
                        .map(OptionResponse::from)
                        .collect(Collectors.toList()))
                .build();
    }
    public void setDetail(String detail) {
        this.detail = detail;
    }

    public void setReviews(ReviewListResponse reviews) {
        this.reviews = reviews;
    }

    public void setQuestions(QnAListResponse questions) {
        this.questions = questions;
    }


}


