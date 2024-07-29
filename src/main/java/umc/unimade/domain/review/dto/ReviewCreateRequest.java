package umc.unimade.domain.review.dto;

import lombok.*;
import org.springframework.web.multipart.MultipartFile;
import umc.unimade.domain.accounts.entity.Buyer;
import umc.unimade.domain.products.entity.Products;
import umc.unimade.domain.review.entity.Review;
import umc.unimade.domain.review.entity.ReviewImage;
import umc.unimade.global.util.s3.S3Provider;
import umc.unimade.global.util.s3.dto.S3UploadRequest;

import java.util.List;
import java.util.stream.Collectors;

@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ReviewCreateRequest {
    private String title;
    private String content;
    private Integer ratingStar;

    public Review toEntity(Products product, Buyer buyer){
        return Review.builder()
                .title(this.title)
                .content(this.content)
                .ratingStar(this.ratingStar)
                .product(product)
                .buyer(buyer)
                .build();
    }
    public List<ReviewImage> toReviewImages(List<MultipartFile> images, S3Provider s3Provider, Long buyerId, Review review) {
        if (images == null || images.isEmpty()) {
            return null;
        }
        return images.stream()
                .map(image -> {
                    String imageUrl = s3Provider.uploadFile(image,
                            S3UploadRequest.builder()
                                    .userId(buyerId)
                                    .dirName("review")
                                    .build());
                    return ReviewImage.builder()
                            .imageUrl(imageUrl)
                            .review(review)
                            .build();
                })
                .collect(Collectors.toList());
    }
}
