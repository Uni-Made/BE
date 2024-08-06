package umc.unimade.domain.notification.events;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class OrderRequestEvent {
    private final Long userId;
}
