package umc.unimade.domain.review.dto;

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
    private String title;
    private String content;
    private List<String> reviewImages;

    public static ReviewResponse to(Review review){
        return ReviewResponse.builder()
                .reviewId(review.getId())
                .title(review.getTitle())
                .content(review.getContent())
                .reviewImages(review.getReviewImages().stream()
                        .map(ReviewImage::getImageUrl)
                        .collect(Collectors.toList()))
                .build();
    }
}
