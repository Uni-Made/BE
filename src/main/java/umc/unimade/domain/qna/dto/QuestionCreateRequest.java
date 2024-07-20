package umc.unimade.domain.qna.dto;
import lombok.*;


@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class QuestionCreateRequest {
    private String title;
    private String content;
}