package umc.unimade.domain.review.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import umc.unimade.domain.review.entity.Review;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ReviewListResponse {
    private Integer totalCount;
    private Double ratingAverage;
    private List<ReviewPreview> reviewsList;
    private Long nextCursor;
    private Boolean isLast;

    @Getter
    @AllArgsConstructor
    @NoArgsConstructor
    @Builder
    public static class ReviewPreview {
        private Long reviewId;
        private String buyer;
        private String title;
        private Integer ratingStar;
        private String imgUrl;
        private LocalDateTime createdAt;

        public static ReviewPreview from(Review review) {
            return ReviewPreview.builder()
                    .reviewId(review.getId())
                    .buyer(review.getBuyer().getName())
                    .title(review.getTitle())
                    .ratingStar(review.getRatingStar())
                    .imgUrl(review.getReviewImages().isEmpty() ? null :review.getReviewImages().get(0).getImageUrl())
                    .createdAt(review.getCreatedAt())
                    .build();
        }
    }

    public static ReviewListResponse from(List<Review> reviewsList, Long nextCursor, Boolean isLast) {
        List<ReviewPreview> reviews = reviewsList.stream()
                .map(ReviewPreview::from)
                .collect(Collectors.toList());
        double ratingAverage = reviewsList.stream()
                .mapToInt(Review::getRatingStar)
                .average()
                .orElse(0.0);
        return ReviewListResponse.builder()
                .totalCount(reviewsList.size())
                .ratingAverage(ratingAverage)
                .reviewsList(reviews)
                .nextCursor(nextCursor)
                .isLast(isLast)
                .build();
    }
}