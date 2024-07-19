package umc.unimade.domain.review.dto;

import lombok.*;
import java.util.List;

@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ReviewCreateRequest {
    private String title;
    private String content;
}
