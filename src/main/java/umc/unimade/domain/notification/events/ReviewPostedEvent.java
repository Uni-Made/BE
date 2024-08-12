package umc.unimade.domain.notification.events;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class ReviewPostedEvent {
    private final Long sellerId;
    private final Long reviewId;
}
