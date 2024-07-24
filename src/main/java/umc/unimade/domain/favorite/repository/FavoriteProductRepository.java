package umc.unimade.domain.favorite.repository;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import umc.unimade.domain.accounts.entity.Buyer;
import umc.unimade.domain.favorite.entity.FavoriteProduct;
import umc.unimade.domain.products.entity.Products;

import java.util.List;
import java.util.Optional;

public interface FavoriteProductRepository extends JpaRepository<FavoriteProduct, Long> {
    Optional<FavoriteProduct> findByProductAndBuyer(Products product, Buyer buyer);

    List<FavoriteProduct> findTop4ByBuyerOrderByCreatedAtDesc(Buyer buyer);

    @Query("SELECT fp FROM FavoriteProduct fp WHERE fp.buyer.id = :buyerId ORDER BY fp.createdAt DESC")
    List<FavoriteProduct> findByBuyerIdOrderByCreatedAtDesc(Long buyerId, Pageable pageable);

    @Query("SELECT fp FROM FavoriteProduct fp WHERE fp.buyer.id = :buyerId AND fp.id < :cursor ORDER BY fp.createdAt DESC")
    List<FavoriteProduct> findByBuyerIdAndIdLessThanOrderByCreatedAtDesc(Long buyerId, Long cursor, Pageable pageable);
}
