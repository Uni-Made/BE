package umc.unimade.domain.accounts.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import umc.unimade.domain.accounts.entity.Buyer;

public interface BuyerRepository extends JpaRepository<Buyer, Long> {
}
