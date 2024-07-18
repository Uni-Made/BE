package umc.unimade.domain.qna.dto;
import lombok.*;
import org.aspectj.weaver.patterns.TypePatternQuestions;
import umc.unimade.domain.qna.entity.Answers;
import umc.unimade.domain.qna.entity.Questions;

import java.util.List;
import java.util.stream.Collectors;

@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class QnAResponse {
        private Long questionId;
        private String title;
        private String content;
        private List<AnswerResponse> answers;

        public static QnAResponse toQnAResponse(Questions question){
            return QnAResponse.builder()
                    .questionId(question.getId())
                    .title(question.getTitle())
                    .content(question.getContent())
                    .answers(question.getAnswers().stream()
                            .map(AnswerResponse::toAnswerResponse)
                            .collect(Collectors.toList()))
                    .build();
        }

    @Getter
    @AllArgsConstructor
    @NoArgsConstructor
    @Builder
    public static class AnswerResponse{
        private Long answerId;
        private String title;
        private String content;

        public static AnswerResponse toAnswerResponse(Answers answer){
            return AnswerResponse.builder()
                    .answerId(answer.getId())
                    .title(answer.getTitle())
                    .content(answer.getContent())
                    .build();
        }
    }
}
