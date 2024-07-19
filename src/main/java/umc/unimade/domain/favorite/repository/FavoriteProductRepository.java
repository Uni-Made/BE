package umc.unimade.domain.favorite.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import umc.unimade.domain.favorite.entity.FavoriteProduct;

public interface FavoriteProductRepository extends JpaRepository<FavoriteProduct, Long> {
}
