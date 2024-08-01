package umc.unimade.domain.review.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import umc.unimade.domain.review.entity.ReviewReport;

public interface ReviewReportRepository extends JpaRepository<ReviewReport, Long> {
}
