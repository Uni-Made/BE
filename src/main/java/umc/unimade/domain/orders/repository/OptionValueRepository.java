package umc.unimade.domain.orders.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import umc.unimade.domain.products.entity.OptionValue;

public interface OptionValueRepository extends JpaRepository<OptionValue, Long> {
}
