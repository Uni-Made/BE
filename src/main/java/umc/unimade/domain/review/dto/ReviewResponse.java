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
    private String profileImage;
    private String title;
    private String content;
    private Integer ratingStar;
    private List<String> reviewImages;
    private LocalDateTime createdAt;
    private List<ReviewAnswerResposne> reviewAnswer;

    public static ReviewResponse from(Review review){
        return ReviewResponse.builder()
                .reviewId(review.getId())
                .buyer(review.getBuyer().getName())
                .profileImage(review.getBuyer().getProfileImage())
                .title(review.getTitle())
                .content(review.getContent())
                .ratingStar(review.getRatingStar())
                .reviewImages(review.getReviewImages().stream()
                        .map(ReviewImage::getImageUrl)
                        .collect(Collectors.toList()))
                .createdAt(review.getCreatedAt())
                .reviewAnswer(review.getReviewAnswers().stream()
                        .map(ReviewAnswerResposne::from)
                        .collect(Collectors.toList()))
                .build();
    }
}
