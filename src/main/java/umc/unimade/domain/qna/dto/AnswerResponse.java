package umc.unimade.domain.qna.dto;

import lombok.*;
import umc.unimade.domain.qna.entity.AnswerImage;
import umc.unimade.domain.qna.entity.Answers;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class AnswerResponse {
    private Long answerId;
    private String seller;
    private String title;
    private String content;
    private List<String> answerImages;
    private LocalDateTime createdAt;

    public static AnswerResponse to(Answers answer){
        return AnswerResponse.builder()
                .answerId(answer.getId())
                .seller(answer.getSeller().getName())
                .title(answer.getTitle())
                .content(answer.getContent())
                .answerImages(answer.getAnswerImages().stream()
                        .map(AnswerImage::getImageUrl)
                        .collect(Collectors.toList()))
                .createdAt(answer.getCreatedAt())
                .build();
    }
}
