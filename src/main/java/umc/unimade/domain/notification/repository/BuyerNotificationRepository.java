package umc.unimade.domain.notification.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import umc.unimade.domain.notification.entity.BuyerNotification;

public interface BuyerNotificationRepository extends JpaRepository<BuyerNotification,Long> {
}
