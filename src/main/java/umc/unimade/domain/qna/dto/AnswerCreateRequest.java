package umc.unimade.domain.qna.dto;

import lombok.*;
import umc.unimade.domain.accounts.entity.Seller;
import umc.unimade.domain.qna.entity.Answers;
import umc.unimade.domain.qna.entity.Questions;

@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class AnswerCreateRequest {
    private String title;
    private String content;

    public Answers toEntity(Seller seller, Questions question) {
        return Answers.builder()
                .title(this.title)
                .content(this.content)
                .seller(seller)
                .question(question)
                .build();
    }
}
