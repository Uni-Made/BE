package umc.unimade.domain.products.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import umc.unimade.domain.products.entity.OptionCategory;

@Repository
public interface OptionCategoryRepository extends JpaRepository<OptionCategory, Long> {
    void deleteAllByProductId(Long id);
}