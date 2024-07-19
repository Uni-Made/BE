package umc.unimade.domain.accounts.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import umc.unimade.domain.accounts.entity.Seller;

public interface SellerRepository extends JpaRepository<Seller, Long> {
}
