package umc.unimade.domain.orders.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import umc.unimade.domain.orders.entity.OrderOption;

public interface OrderOptionRepository extends JpaRepository <OrderOption, Long> {
}
