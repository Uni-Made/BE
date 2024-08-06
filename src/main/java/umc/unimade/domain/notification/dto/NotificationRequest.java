package umc.unimade.domain.notification.dto;
import lombok.*;

@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class NotificationRequest {
    private String title;
    private String body;
    private String id;
}
