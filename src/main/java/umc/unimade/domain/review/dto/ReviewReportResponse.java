package umc.unimade.domain.review.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import umc.unimade.domain.review.entity.ReportType;
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
    private String description;

    public static ReviewReportResponse from(ReviewReport report) {
        return ReviewReportResponse.builder()
                .reportId(report.getId())
                .reviewId(report.getReview().getId())
                .buyerId(report.getReview().getBuyer().getId())
                .sellerId(report.getSeller().getId())
                .type(report.getType())
                .description(report.getDescription())
                .build();
    }

}
