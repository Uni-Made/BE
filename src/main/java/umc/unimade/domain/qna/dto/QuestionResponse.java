package umc.unimade.domain.qna.dto;

import lombok.*;
import umc.unimade.domain.qna.entity.Questions;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class QuestionResponse {
    private Long questionId;
    private String buyer;
    private String profileImage;
    private String title;
    private String content;
    private Boolean isPrivate;
    private LocalDateTime createdAt;
    private boolean isAnswered;
    private List<AnswerResponse> answer;

    public static QuestionResponse from(Questions question){
        return QuestionResponse.builder()
                .questionId(question.getId())
                .buyer(question.getBuyer().getName())
                .profileImage(question.getBuyer().getProfileImage())
                .title(question.getTitle())
                .content(question.getContent())
                .isPrivate(question.getIsPrivate())
                .createdAt(question.getCreatedAt())
                .isAnswered(!question.getAnswers().isEmpty())
                .answer(question.getAnswers().stream()
                        .map(AnswerResponse::from)
                        .collect(Collectors.toList()))
                .build();
    }
}
