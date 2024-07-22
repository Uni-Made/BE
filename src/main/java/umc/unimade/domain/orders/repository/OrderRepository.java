package umc.unimade.domain.orders.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import umc.unimade.domain.orders.entity.Orders;

import java.util.List;

public interface OrderRepository extends JpaRepository<Orders, Long> {

    @Query("SELECT o FROM Orders o WHERE o.product.seller.id = :sellerId")
    List<Orders> findOrdersBySellerId(Long sellerId);
}
