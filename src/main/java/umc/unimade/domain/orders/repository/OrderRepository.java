package umc.unimade.domain.orders.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import umc.unimade.domain.orders.entity.Orders;

public interface OrderRepository extends JpaRepository<Orders, Long> {
}
