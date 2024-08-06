package umc.unimade.domain.notification.repository;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import umc.unimade.domain.accounts.entity.Buyer;
import umc.unimade.domain.favorite.entity.FavoriteProduct;
import umc.unimade.domain.notification.dto.NotificationListResponse;
import umc.unimade.domain.notification.entity.BuyerNotification;

import java.time.LocalDateTime;
import java.util.List;

public interface BuyerNotificationRepository extends JpaRepository<BuyerNotification, Long> {
    void deleteAllByCreatedAtBefore(LocalDateTime timeLimit);

    @Query("SELECT bn FROM BuyerNotification bn WHERE bn.buyer = :buyer ORDER BY bn.createdAt DESC")
    List<BuyerNotification> findByBuyerOrderByCreatedAtDesc(Buyer buyer, Pageable pageable);

    @Query("SELECT bn FROM BuyerNotification bn WHERE bn.buyer = :buyer AND bn.id < :cursor ORDER BY bn.createdAt DESC")
    List<BuyerNotification> findByBuyerAndIdLessThanOrderByCreatedAtDesc(Buyer buyer, Long cursor, Pageable pageable);
}
