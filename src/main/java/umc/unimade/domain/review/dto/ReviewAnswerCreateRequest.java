package umc.unimade.domain.review.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import umc.unimade.domain.accounts.entity.Seller;
import umc.unimade.domain.review.entity.Review;
import umc.unimade.domain.review.entity.ReviewAnswer;

@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ReviewAnswerCreateRequest {
    private String title;
    private String content;

    public ReviewAnswer toEntity(Seller seller, Review review) {
        return ReviewAnswer.builder()
                .title(title)
                .content(content)
                .seller(seller)
                .review(review)
                .build();
    }
}
