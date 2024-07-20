package umc.unimade.domain.review.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import umc.unimade.domain.review.entity.Review;
import umc.unimade.domain.review.entity.ReviewImage;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ReviewListResponse {
    private Long reviewId;
    private String buyer;
    private String title;
    private LocalDateTime createdAt;

    public static ReviewListResponse from(Review review){
        return ReviewListResponse.builder()
                .reviewId(review.getId())
                .buyer(review.getBuyer().getName())
                .title(review.getTitle())
                .createdAt(review.getCreatedAt())
                .build();
    }
}
