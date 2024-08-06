package umc.unimade.domain.review.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import umc.unimade.domain.review.entity.ReportStatus;
import umc.unimade.domain.review.entity.ReviewReport;

import java.time.LocalDateTime;

@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ReportProcessResponse {
    private Long id;
    private Long reviewId;
    private ReportStatus status;
    private LocalDateTime processedAt;

    public static ReportProcessResponse from(ReviewReport report) {
        return ReportProcessResponse.builder()
                .id(report.getId())
                .reviewId(report.getReview().getId())
                .status(report.getStatus())
                .processedAt(report.getProcessedAt())
                .build();
    }
}
