package umc.unimade.domain.favorite.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import umc.unimade.domain.accounts.entity.Buyer;
import umc.unimade.domain.favorite.entity.FavoriteProduct;
import umc.unimade.domain.products.entity.Products;

import java.util.List;
import java.util.Optional;

public interface FavoriteProductRepository extends JpaRepository<FavoriteProduct, Long> {
    Optional<FavoriteProduct> findByProductAndBuyer(Products product, Buyer buyer);

    List<FavoriteProduct> findTop4ByBuyerOrderByCreatedAtDesc(Buyer buyer);
}
