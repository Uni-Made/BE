package umc.unimade.domain.review.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import umc.unimade.domain.review.entity.Review;
import umc.unimade.domain.review.entity.ReviewReport;

@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ReviewReportsResponse {

    private Long reportId;
    private Long reviewId;
    private String reviewTitle;

    public static ReviewReportsResponse from(ReviewReport report) {
        Review review = report.getReview();
        return ReviewReportsResponse.builder()
                .reportId(report.getId())
                .reviewId(review.getId())
                .reviewTitle(review.getTitle())
                .build();
    }
}
