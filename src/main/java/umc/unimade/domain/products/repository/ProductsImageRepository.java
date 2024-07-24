package umc.unimade.domain.products.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import umc.unimade.domain.products.entity.ProductsImage;

@Repository
public interface ProductsImageRepository extends JpaRepository<ProductsImage, Long> {
    @Transactional
    @Modifying
    @Query("delete from ProductsImage p where p.product.id = ?1")
    void deleteAllByProductId(Long id);
}