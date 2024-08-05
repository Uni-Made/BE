package umc.unimade.domain.products.dto;



import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.*;
import java.util.List;

import umc.unimade.domain.products.entity.PickupOption;
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
@JsonInclude(JsonInclude.Include.NON_NULL)
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
    private PickupOption pickupOption;
    private ProductDetailResponse detail;
    private ReviewListResponse reviews;
    private QnAListResponse questions;

    public static ProductResponse baseResponse (Products product){
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
                .productImages(product.getProductImages().stream()
                        .map(ProductsImage::getImageUrl)
                        .collect(Collectors.toList()))
                .favoriteCount(product.getFavoriteProducts().size())
                .options(product.getOptionCategories().stream()
                        .map(OptionResponse::from)
                        .collect(Collectors.toList()))
                .pickupOption(product.getPickupOption())
                .build();
    }

    public static ProductResponse toDetail(Products product,ProductDetailResponse detail) {
        ProductResponse response = baseResponse(product);
        response.setDetail(detail);
        return response;
    }

    public static ProductResponse toReview(Products product,ReviewListResponse reviews) {
        ProductResponse response = baseResponse(product);
        response.setReviews(reviews);
        return response;
    }

    public static ProductResponse toQnA(Products product,QnAListResponse questions) {
        ProductResponse response = baseResponse(product);
        response.setQuestions(questions);
        return response;
    }


    public void setDetail(ProductDetailResponse detail) {
        this.detail = detail;
    }

    public void setReviews(ReviewListResponse reviews) {
        this.reviews = reviews;
    }

    public void setQuestions(QnAListResponse questions) {
        this.questions = questions;
    }






}


