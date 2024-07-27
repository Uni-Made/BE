package umc.unimade.domain.review.repository;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import umc.unimade.domain.review.entity.Review;

import java.util.List;

public interface ReviewRepository extends JpaRepository<Review, Long> {
    @Query("SELECT r FROM Review r WHERE r.product.id = :productId AND (:cursor IS NULL OR r.id < :cursor) ORDER BY r.id DESC")
    List<Review> findByProductIdWithCursorPagination(@Param("productId") Long productId, @Param("cursor") Long cursor, Pageable pageable);
}
