package umc.unimade.domain.favorite.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import umc.unimade.domain.accounts.entity.Buyer;
import umc.unimade.domain.accounts.entity.Seller;
import umc.unimade.domain.favorite.entity.FavoriteSeller;

import java.util.List;
import java.util.Optional;


public interface FavoriteSellerRepository extends JpaRepository<FavoriteSeller, Long> {
    Optional<FavoriteSeller> findBySellerAndBuyer(Seller seller, Buyer buyer);

    List<FavoriteSeller> findTop4ByBuyerOrderByCreatedAtDesc(Buyer buyer);
}
