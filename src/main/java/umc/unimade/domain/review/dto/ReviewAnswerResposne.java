package umc.unimade.domain.review.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import umc.unimade.domain.review.entity.ReviewAnswer;

import java.time.LocalDateTime;

@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ReviewAnswerResposne {
    private Long reviewAnswerId;
    private String buyer;
    private String title;
    private String content;
    private LocalDateTime createdAt;

    public static ReviewAnswerResposne from(ReviewAnswer reviewAnswer){
        return ReviewAnswerResposne.builder()
                .reviewAnswerId(reviewAnswer.getId())
                .buyer(reviewAnswer.getSeller().getName())
                .title(reviewAnswer.getTitle())
                .content(reviewAnswer.getContent())
                .createdAt(reviewAnswer.getCreatedAt())
                .build();
    }
}

