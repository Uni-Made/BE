package umc.unimade.domain.qna.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import umc.unimade.domain.qna.entity.QuestionImage;
import umc.unimade.domain.qna.entity.Questions;
import java.time.LocalDateTime;
import java.util.stream.Collectors;
import java.util.List;


@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class QuestionResponse {
    private Long questionId;
    private String buyer;
    private String title;
    private String content;
    private List<String> questionImages;
    private LocalDateTime createdAt;

    public static QuestionResponse from(Questions question){
        return QuestionResponse.builder()
                .questionId(question.getId())
                .buyer(question.getBuyer().getName())
                .title(question.getTitle())
                .content(question.getContent())
                .questionImages(question.getQuestionImages().stream()
                        .map(QuestionImage::getImageUrl)
                        .collect(Collectors.toList()))
                .createdAt(question.getCreatedAt())
                .build();
    }
}
