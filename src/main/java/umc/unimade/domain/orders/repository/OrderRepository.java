package umc.unimade.domain.orders.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import umc.unimade.domain.accounts.entity.Buyer;
import umc.unimade.domain.orders.entity.OrderStatus;
import umc.unimade.domain.orders.entity.Orders;

import java.time.LocalDateTime;
import java.util.List;

public interface OrderRepository extends JpaRepository<Orders, Long> {

    @Query("SELECT o FROM Orders o WHERE o.product.seller.id = :sellerId")
    Page<Orders> findOrdersBySellerId(Long sellerId, Pageable pageable);

    @Query("SELECT o FROM Orders o WHERE o.product.id = :productId")
    Page<Orders> findOrdersByProductId(Long productId, Pageable pageable);
  
    @Query("SELECT o FROM Orders o WHERE o.buyer = :buyer AND (:cursor IS NULL OR o.id < :cursor) ORDER BY o.id DESC")
    List<Orders> findOrdersByBuyerWithCursorPagination(Buyer buyer, @Param("cursor") Long cursor, Pageable pageable);

    @Query("SELECT o FROM Orders o WHERE o.purchaseForm.createdAt < :threeDaysAgo AND o.status = :status")
    List<Orders> findOrdersCreatedBefore(LocalDateTime threeDaysAgo, OrderStatus status);
}