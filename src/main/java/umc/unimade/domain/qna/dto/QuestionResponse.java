package umc.unimade.domain.qna.dto;

import lombok.*;
import umc.unimade.domain.qna.entity.Questions;
import java.time.LocalDateTime;


@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class QuestionResponse {
    private Long questionId;
    private String buyer;
    private String title;
    private String content;
    private Boolean isPrivate;
    private LocalDateTime createdAt;

    public static QuestionResponse from(Questions question){
        return QuestionResponse.builder()
                .questionId(question.getId())
                .buyer(question.getBuyer().getName())
                .title(question.getTitle())
                .content(question.getContent())
                .isPrivate(question.getIsPrivate())
                .createdAt(question.getCreatedAt())
                .build();
    }
}
