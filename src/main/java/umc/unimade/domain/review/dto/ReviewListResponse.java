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
    private List<ReviewInfo> reviewsList;
    private Long nextCursor;
    private Boolean isLast;

    @Getter
    @AllArgsConstructor
    @NoArgsConstructor
    @Builder
    public static class ReviewInfo {
        private Long reviewId;
        private String buyer;
        private String title;
        private LocalDateTime createdAt;

        public static ReviewInfo from(Review review) {
            return ReviewInfo.builder()
                    .reviewId(review.getId())
                    .buyer(review.getBuyer().getName())
                    .title(review.getTitle())
                    .createdAt(review.getCreatedAt())
                    .build();
        }
    }

    public static ReviewListResponse from(List<Review> reviewsList, Long nextCursor, Boolean isLast) {
        List<ReviewInfo> reviews = reviewsList.stream()
                .map(ReviewInfo::from)
                .collect(Collectors.toList());
        return ReviewListResponse.builder()
                .reviewsList(reviews)
                .nextCursor(nextCursor)
                .isLast(isLast)
                .build();
    }
}