package umc.unimade.domain.products.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import umc.unimade.domain.products.entity.ProductStatus;
import umc.unimade.domain.products.entity.Products;

import java.util.List;

@Repository
public interface ProductRepository extends JpaRepository<Products, Long>, ProductsRepositoryCustom {
    List<Products> findTop4BySellerIdAndStatusOrderByCreatedAtDesc(Long sellerId, ProductStatus status);

    @Query("SELECT p FROM Products p WHERE p.status = :status AND p.deadline < CURRENT_DATE")
    List<Products> findExpiredProducts(ProductStatus status);
}