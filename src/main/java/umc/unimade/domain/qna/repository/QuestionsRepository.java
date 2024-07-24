package umc.unimade.domain.qna.repository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import umc.unimade.domain.qna.entity.Questions;

import java.util.List;

public interface QuestionsRepository extends JpaRepository<Questions, Long> {
    @Query("SELECT q FROM Questions q WHERE q.product.id = :productId AND (:cursor IS NULL OR q.id < :cursor) ORDER BY q.id DESC")
    List<Questions> findByProductIdWithCursorPagination(@Param("productId") Long productId, @Param("cursor") Long cursor, Pageable pageable);
}