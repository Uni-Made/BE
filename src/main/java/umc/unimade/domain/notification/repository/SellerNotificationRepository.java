package umc.unimade.domain.notification.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import umc.unimade.domain.notification.entity.SellerNotification;

public interface SellerNotificationRepository extends JpaRepository<SellerNotification,Long> {
}
