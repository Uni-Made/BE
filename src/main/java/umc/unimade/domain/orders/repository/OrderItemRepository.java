package umc.unimade.domain.orders.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import umc.unimade.domain.orders.entity.OrderItem;

public interface OrderItemRepository extends JpaRepository<OrderItem, Long> {
}
