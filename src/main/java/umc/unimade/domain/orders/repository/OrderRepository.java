package umc.unimade.domain.orders.repository;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import umc.unimade.domain.orders.entity.Orders;

import java.util.List;

public interface OrderRepository extends JpaRepository<Orders, Long> {

    @Query("SELECT o FROM Orders o WHERE o.product.seller.id = :sellerId")
    List<Orders> findOrdersBySellerId(Long sellerId, Pageable pageable);
  
    @Query("SELECT o FROM Orders o WHERE o.buyer.id = :buyerId AND (:cursor IS NULL OR o.id < :cursor) ORDER BY o.id DESC")
    List<Orders> findOrdersByBuyerIdWithCursorPagination(@Param("buyerId") Long buyerId, @Param("cursor") Long cursor, Pageable pageable);
}
