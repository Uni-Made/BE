package umc.unimade.domain.review.dto;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

import lombok.*;
import umc.unimade.domain.review.entity.Review;
import umc.unimade.domain.review.entity.ReviewImage;

@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ReviewResponse {
    private Long reviewId;
    private String buyer;
    private String title;
    private String content;
    private List<String> reviewImages;
    private LocalDateTime createdAt;

    public static ReviewResponse to(Review review){
        return ReviewResponse.builder()
                .reviewId(review.getId())
                .buyer(review.getBuyer().getName())
                .title(review.getTitle())
                .content(review.getContent())
                .reviewImages(review.getReviewImages().stream()
                        .map(ReviewImage::getImageUrl)
                        .collect(Collectors.toList()))
                .createdAt(review.getCreatedAt())
                .build();
    }
}
