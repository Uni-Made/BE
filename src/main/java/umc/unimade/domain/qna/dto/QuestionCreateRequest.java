package umc.unimade.domain.qna.dto;
import lombok.*;
import umc.unimade.domain.accounts.entity.Buyer;
import umc.unimade.domain.products.entity.Products;
import umc.unimade.domain.qna.entity.Questions;


@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class QuestionCreateRequest {
    private String title;
    private String content;
    private Boolean isPrivate;

    public Questions toEntity(Products product, Buyer buyer) {
        return Questions.builder()
                .title(this.title)
                .content(this.content)
                .isPrivate(this.isPrivate)
                .product(product)
                .buyer(buyer)
                .build();
    }
}