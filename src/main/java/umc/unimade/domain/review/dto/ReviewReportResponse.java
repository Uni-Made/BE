package umc.unimade.domain.review.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import umc.unimade.domain.review.entity.ReportType;
import umc.unimade.domain.review.entity.Review;
import umc.unimade.domain.review.entity.ReviewReport;

@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ReviewReportResponse {

    private Long reportId;
    private Long reviewId;
    private Long buyerId;
    private Long sellerId;
    private ReportType type;
    private String reviewTitle;
    private String reviewContent;
    private String description;

    public static ReviewReportResponse from(ReviewReport report) {
        Review review = report.getReview();
        return ReviewReportResponse.builder()
                .reportId(report.getId())
                .reviewId(review.getId())
                .buyerId(review.getBuyer().getId())
                .sellerId(report.getSeller().getId())
                .type(report.getType())
                .reviewTitle(review.getTitle())
                .reviewContent(review.getContent())
                .description(report.getDescription())
                .build();
    }

}
