package umc.unimade.domain.review.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import umc.unimade.domain.review.entity.ReviewAnswer;

public interface ReviewAnswerRepository extends JpaRepository<ReviewAnswer, Long> {
}
