package umc.unimade.domain.qna.repository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import umc.unimade.domain.qna.entity.Questions;

public interface QuestionsRepository extends JpaRepository<Questions, Long> {
    Page<Questions> findByProductId(Long productId, Pageable pageable);
}