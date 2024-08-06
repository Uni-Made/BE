package umc.unimade.domain.review.dto;

import lombok.*;
import org.springframework.web.multipart.MultipartFile;
import umc.unimade.domain.orders.entity.Orders;
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

    public Review toEntity(Orders order){
        return Review.builder()
                .title(this.title)
                .content(this.content)
                .ratingStar(this.ratingStar)
                .product(order.getProduct())
                .order(order)
                .buyer(order.getBuyer())
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
