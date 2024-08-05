package umc.unimade.domain.notification.events;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import umc.unimade.domain.orders.entity.Orders;

@Getter
@RequiredArgsConstructor
public class ReviewRequestEvent {
    private final Long userId;
    private final Orders order;
}
