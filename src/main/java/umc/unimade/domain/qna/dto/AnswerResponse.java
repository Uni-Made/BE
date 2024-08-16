package umc.unimade.domain.qna.dto;

import lombok.*;
import umc.unimade.domain.qna.entity.Answers;
import java.time.LocalDateTime;


@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class AnswerResponse {
    private Long answerId;
    private String seller;
    private String content;
    private Boolean isPrivate;
    private LocalDateTime createdAt;

    public static AnswerResponse from(Answers answer){
        return AnswerResponse.builder()
                .answerId(answer.getId())
                .seller(answer.getSeller().getName())
                .content(answer.getContent())
                .isPrivate(answer.getIsPrivate())
                .createdAt(answer.getCreatedAt())
                .build();
    }
}
