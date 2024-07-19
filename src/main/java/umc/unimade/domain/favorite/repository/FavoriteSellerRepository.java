package umc.unimade.domain.favorite.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import umc.unimade.domain.favorite.entity.FavoriteSeller;


public interface FavoriteSellerRepository extends JpaRepository<FavoriteSeller, Long> {
}
