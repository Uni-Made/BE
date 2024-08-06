package umc.unimade.domain.review.service;

import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import umc.unimade.domain.accounts.entity.Buyer;
import umc.unimade.domain.review.dto.ReportProcessResponse;
import umc.unimade.domain.review.entity.ReportStatus;
import umc.unimade.domain.review.entity.ReviewReport;
import umc.unimade.domain.review.exception.ReviewExceptionHandler;
import umc.unimade.domain.review.repository.ReviewReportRepository;
import umc.unimade.global.common.ErrorCode;

import java.time.LocalDateTime;
import java.util.List;

@Transactional
@Service
@RequiredArgsConstructor
public class ReviewReportCommandService {

    private final ReviewReportRepository reviewReportRepository;

    public ReportProcessResponse processReport(Long reportId, ReportStatus newStatus) {
        ReviewReport report = reviewReportRepository.findById(reportId)
                .orElseThrow(() -> new ReviewExceptionHandler(ErrorCode.REPORT_NOT_FOUND));
        report.processReport(LocalDateTime.now(), newStatus);
        Buyer buyer = report.getReview().getBuyer();
        buyer.changeStatus(newStatus);
        return ReportProcessResponse.from(report);
    }

    @Scheduled(cron = "0 0 0 * * ?") // 매일 자정에 실행
    public void updateReportStatuses() {
        List<ReviewReport> reports = reviewReportRepository.findAll();

        for (ReviewReport report : reports) {
            if (report.getStatus() != ReportStatus.FREE && report.getProcessedAt() != null) {
                Buyer buyer = report.getReview().getBuyer();
                LocalDateTime processedAt = report.getProcessedAt();
                LocalDateTime now = LocalDateTime.now();

                switch (report.getStatus()) {
                    case THREE:
                        if (processedAt.plusDays(3).isBefore(now)) {
                            report.changeStatus(ReportStatus.FREE);
                            buyer.changeStatus(ReportStatus.FREE);
                        }
                        break;
                    case WEEK:
                        if (processedAt.plusWeeks(1).isBefore(now)) {
                            report.changeStatus(ReportStatus.FREE);
                            buyer.changeStatus(ReportStatus.FREE);
                        }
                        break;
                    case MONTH:
                        if (processedAt.plusMonths(1).isBefore(now)) {
                            report.changeStatus(ReportStatus.FREE);
                            buyer.changeStatus(ReportStatus.FREE);
                        }
                        break;
                    case PERMANENT:
                        // PERMANENT 상태는 변경하지 않습니다.
                        break;
                    default:
                        break;
                }
                reviewReportRepository.save(report);
            }
        }
    }

}
