package umc.unimade.domain.orders.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import umc.unimade.domain.orders.entity.PurchaseForm;

public interface PurchaseFormRepository extends JpaRepository<PurchaseForm, Long> {
}
