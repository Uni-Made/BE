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
    private List<QuestionInfo> questionsList;
    private Long nextCursor;
    private Boolean isLast;

    @Getter
    @AllArgsConstructor
    @NoArgsConstructor
    @Builder
    public static class QuestionInfo {
        private Long questionId;
        private String title;
        private String buyer;
        private Boolean isPrivate;
        private LocalDateTime createdAt;
        private List<AnswerInfo> answers;

        public static QuestionInfo from(Questions question) {
            return QuestionInfo.builder()
                    .questionId(question.getId())
                    .title(question.getTitle())
                    .buyer(question.getBuyer().getName())
                    .isPrivate(question.getIsPrivate())
                    .createdAt(question.getCreatedAt())
                    .answers(question.getAnswers().stream()
                            .map(AnswerInfo::from)
                            .collect(Collectors.toList()))
                    .build();
        }
    }

    @Getter
    @AllArgsConstructor
    @NoArgsConstructor
    @Builder
    public static class AnswerInfo {
        private Long answerId;
        private String title;
        private String seller;
        private Boolean isPrivate;
        private LocalDateTime createdAt;

        public static AnswerInfo from(Answers answer) {
            return AnswerInfo.builder()
                    .answerId(answer.getId())
                    .title(answer.getTitle())
                    .seller(answer.getSeller().getName())
                    .isPrivate(answer.getIsPrivate())
                    .createdAt(answer.getCreatedAt())
                    .build();
        }
    }

    public static QnAListResponse from(List<Questions> questionsList, Long nextCursor, Boolean isLast) {
        List<QuestionInfo> questions = questionsList.stream()
                .map(QuestionInfo::from)
                .collect(Collectors.toList());
        return QnAListResponse.builder()
                .questionsList(questions)
                .nextCursor(nextCursor)
                .isLast(isLast)
                .build();
    }
}
