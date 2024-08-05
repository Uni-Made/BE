package umc.unimade.domain.notification.events;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.time.LocalDateTime;

@Getter
@RequiredArgsConstructor
public class PaymentRequestEvent {
    private final Long orderId;
    private final LocalDateTime purchaseTime;
}
