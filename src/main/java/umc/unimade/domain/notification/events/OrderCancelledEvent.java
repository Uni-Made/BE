package umc.unimade.domain.notification.events;


import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class OrderCancelledEvent {
    private final Long userId;
    private final Long orderId;
}
