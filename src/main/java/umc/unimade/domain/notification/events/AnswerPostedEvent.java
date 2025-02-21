package umc.unimade.domain.notification.events;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class AnswerPostedEvent {
    private final Long questionId;
    private final Long userId;
}