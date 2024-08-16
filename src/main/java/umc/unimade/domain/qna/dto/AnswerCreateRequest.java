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
    private String content;
    private Boolean isPrivate;

    public Answers toEntity(Seller seller, Questions question) {
        return Answers.builder()
                .content(this.content)
                .isPrivate(this.isPrivate)
                .seller(seller)
                .question(question)
                .build();
    }
}
