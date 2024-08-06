package umc.unimade.domain.notification.dto;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import umc.unimade.domain.notification.entity.BuyerNotification;
import umc.unimade.domain.qna.dto.QnAListResponse;
import umc.unimade.domain.qna.entity.Questions;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class NotificationListResponse {
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
        private String extraId;

        public static NotificationDetail from(BuyerNotification notification) {
            return NotificationDetail.builder()
                    .id(notification.getId())
                    .title(notification.getTitle())
                    .body(notification.getBody())
                    .extraId(notification.getExtraId())
                    .build();
        }
    }
    public static NotificationListResponse from(List<BuyerNotification> notificationList,Long nextCursor, Boolean isLast) {
        List<NotificationDetail> notifications = notificationList.stream()
                .map(NotificationDetail::from)
                .collect(Collectors.toList());
        return NotificationListResponse.builder()
                .notificationList(notifications)
                .nextCursor(nextCursor)
                .isLast(isLast)
                .build();
    }


}
