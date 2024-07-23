package umc.unimade.domain.products.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import umc.unimade.domain.products.entity.ProductStatus;
import umc.unimade.domain.products.entity.Products;

import java.util.List;

@Repository
public interface ProductRepository extends JpaRepository<Products, Long> {
    List<Products> findTop4BySellerIdAndStatusOrderByCreatedAtDesc(Long sellerId,  ProductStatus status);
}
