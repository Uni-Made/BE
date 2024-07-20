package umc.unimade.domain.qna.dto;
import lombok.*;
import umc.unimade.domain.qna.entity.Answers;
import umc.unimade.domain.qna.entity.Questions;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class QnAListResponse {
        private Long questionId;
        private String title;
        private String buyer;
        private LocalDateTime createdAt;
        private List<AnswerListResponse> answers;

        public static QnAListResponse from(Questions question){
            return QnAListResponse.builder()
                    .questionId(question.getId())
                    .title(question.getTitle())
                    .buyer(question.getBuyer().getName())
                    .createdAt(question.getCreatedAt())
                    .answers(question.getAnswers().stream()
                            .map(AnswerListResponse::to)
                            .collect(Collectors.toList()))
                    .build();
        }

    @Getter
    @AllArgsConstructor
    @NoArgsConstructor
    @Builder
    public static class AnswerListResponse{
        private Long answerId;
        private String title;
        private String seller;
        private LocalDateTime createdAt;
        public static AnswerListResponse to(Answers answer){
            return AnswerListResponse.builder()
                    .answerId(answer.getId())
                    .title(answer.getTitle())
                    .seller(answer.getSeller().getName())
                    .createdAt(answer.getCreatedAt())
                    .build();
        }
    }
}
