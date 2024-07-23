package umc.unimade.domain.qna.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.web.multipart.MultipartFile;
import umc.unimade.domain.accounts.entity.Seller;
import umc.unimade.domain.qna.entity.AnswerImage;
import umc.unimade.domain.qna.entity.Answers;
import umc.unimade.domain.qna.entity.Questions;
import umc.unimade.global.util.s3.S3Provider;
import umc.unimade.global.util.s3.dto.S3UploadRequest;

import java.util.List;
import java.util.stream.Collectors;

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
