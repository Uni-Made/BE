package umc.unimade.domain.favorite.repository;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import umc.unimade.domain.accounts.entity.Buyer;
import umc.unimade.domain.accounts.entity.Seller;
import umc.unimade.domain.favorite.entity.FavoriteProduct;
import umc.unimade.domain.favorite.entity.FavoriteSeller;

import java.util.List;
import java.util.Optional;


public interface FavoriteSellerRepository extends JpaRepository<FavoriteSeller, Long> {
    Optional<FavoriteSeller> findBySellerAndBuyer(Seller seller, Buyer buyer);

    List<FavoriteSeller> findTop4ByBuyerOrderByCreatedAtDesc(Buyer buyer);

    @Query("SELECT fs FROM FavoriteSeller fs WHERE fs.buyer = :buyer ORDER BY fs.createdAt DESC")
    List<FavoriteSeller> findByBuyerOrderByCreatedAtDesc(Buyer buyer, Pageable pageable);

    @Query("SELECT fs FROM FavoriteSeller fs WHERE fs.buyer = :buyer AND fs.id < :cursor ORDER BY fs.createdAt DESC")
    List<FavoriteSeller> findByBuyerAndIdLessThanOrderByCreatedAtDesc(Buyer buyer, Long cursor, Pageable pageable);
}
