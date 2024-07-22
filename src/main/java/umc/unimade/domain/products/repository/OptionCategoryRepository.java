package umc.unimade.domain.products.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import umc.unimade.domain.products.entity.OptionCategory;

@Repository
public interface OptionCategoryRepository extends JpaRepository<OptionCategory, Long> {
    @Modifying
    @Transactional
    @Query("DELETE FROM OptionCategory oCategory WHERE oCategory.product.id = :productId")
    void deleteByProductId(Long productId);
}