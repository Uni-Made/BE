package umc.unimade.domain.products.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import umc.unimade.domain.products.entity.Category;

@Repository
public interface CategoryRepository extends JpaRepository<Category, Long> {
}