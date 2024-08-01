package umc.unimade.domain.review.dto;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import umc.unimade.domain.accounts.entity.Seller;
import umc.unimade.domain.review.entity.ReportType;
import umc.unimade.domain.review.entity.Review;
import umc.unimade.domain.review.entity.ReviewReport;

@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ReviewReportRequest {

    @NotNull
    private ReportType type;
    private String description;

    public ReviewReport toEntity(Review review, Seller seller) {
        return ReviewReport.builder()
                .review(review)
                .seller(seller)
                .type(type)
                .description(description)
                .build();
    }
}
