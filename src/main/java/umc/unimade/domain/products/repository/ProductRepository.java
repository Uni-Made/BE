package umc.unimade.domain.products.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import umc.unimade.domain.products.entity.ProductStatus;
import umc.unimade.domain.products.entity.Products;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Page;

import java.util.List;

@Repository
public interface ProductRepository extends JpaRepository<Products, Long>, ProductsRepositoryCustom {
    List<Products> findTop4BySellerIdAndStatusOrderByCreatedAtDesc(Long sellerId, ProductStatus status);

    // deadline 지난 selling상품 찾기
    @Query("SELECT p FROM Products p WHERE p.status = :status AND p.deadline < CURRENT_DATE")
    List<Products> findExpiredProducts(ProductStatus status);

    // 인기순
    @Query("SELECT p FROM Products p LEFT JOIN p.favoriteProducts f WHERE p.seller.id = :sellerId AND p.status = :status GROUP BY p.id ORDER BY COUNT(f) DESC")
    Page<Products> findBySellerIdAndStatusOrderByPopularity(Long sellerId, ProductStatus status, Pageable pageable);

    // 최신순
    Page<Products> findBySellerIdAndStatusOrderByCreatedAtDesc(Long sellerId, ProductStatus status, Pageable pageable);

    // 마감순
    @Query("SELECT p FROM Products p WHERE p.seller.id = :sellerId AND p.status = :status ORDER BY p.deadline ASC")
    Page<Products> findBySellerIdAndStatusOrderByDeadline(Long sellerId, ProductStatus status, Pageable pageable);
}