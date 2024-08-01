package umc.unimade.domain.review.service;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import umc.unimade.domain.review.dto.ReviewReportResponse;
import umc.unimade.domain.review.dto.ReviewReportsResponse;
import umc.unimade.domain.review.entity.ReviewReport;
import umc.unimade.domain.review.exception.ReviewExceptionHandler;
import umc.unimade.domain.review.repository.ReviewReportRepository;
import umc.unimade.global.common.ErrorCode;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class ReviewReportQueryService {

    private final ReviewReportRepository reviewReportRepository;

    public Page<ReviewReportsResponse> getReports(Pageable pageable) {
        Page<ReviewReport> reports = reviewReportRepository.findAll(pageable);
        return reports.map(ReviewReportsResponse::from);
    }

    public ReviewReportResponse getReport(Long reviewReportId) {
        ReviewReport report = reviewReportRepository.findById(reviewReportId)
                .orElseThrow(() -> new ReviewExceptionHandler(ErrorCode.REPORT_NOT_FOUND));
        return ReviewReportResponse.from(report);
    }
}
