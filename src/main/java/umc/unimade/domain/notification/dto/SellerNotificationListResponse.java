package umc.unimade.domain.notification.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import umc.unimade.domain.notification.entity.SellerNotification;

import java.util.List;
import java.util.stream.Collectors;

@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class SellerNotificationListResponse {
    private List<NotificationDetail> notificationList;
    private Long nextCursor;
    private Boolean isLast;

    @Getter
    @AllArgsConstructor
    @NoArgsConstructor
    @Builder
    public static class NotificationDetail {
        private Long id;
        private String title;
        private String body;

        public static NotificationDetail from(SellerNotification notification) {
            return NotificationDetail.builder()
                    .id(notification.getId())
                    .title(notification.getTitle())
                    .body(notification.getBody())
                    .build();
        }
    }
    public static SellerNotificationListResponse from(List<SellerNotification> notificationList, Long nextCursor, Boolean isLast) {
        List<NotificationDetail> notifications = notificationList.stream()
                .map(NotificationDetail::from)
                .collect(Collectors.toList());
        return SellerNotificationListResponse.builder()
                .notificationList(notifications)
                .nextCursor(nextCursor)
                .isLast(isLast)
                .build();
    }
}
