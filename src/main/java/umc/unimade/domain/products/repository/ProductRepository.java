package umc.unimade.domain.products.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import umc.unimade.domain.products.entity.Products;
@Repository
public interface ProductRepository extends JpaRepository<Products, Long> {
}
