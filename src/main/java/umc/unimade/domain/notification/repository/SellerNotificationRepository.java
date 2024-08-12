package umc.unimade.domain.notification.repository;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import umc.unimade.domain.accounts.entity.Seller;
import umc.unimade.domain.notification.entity.SellerNotification;

import java.time.LocalDateTime;
import java.util.List;

public interface SellerNotificationRepository extends JpaRepository<SellerNotification, Long> {
    void deleteAllByCreatedAtBefore(LocalDateTime timeLimit);

    @Query("SELECT sn FROM SellerNotification sn WHERE sn.seller = :seller ORDER BY sn.createdAt DESC")
    List<SellerNotification> findBySellerOrderByCreatedAtDesc(Seller seller, Pageable pageable);

    @Query("SELECT sn FROM SellerNotification sn WHERE sn.seller = :seller AND sn.id < :cursor ORDER BY sn.createdAt DESC")
    List<SellerNotification> findBySellerAndIdLessThanOrderByCreatedAtDesc(Seller seller, Long cursor, Pageable pageable);
}
