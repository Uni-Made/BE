package umc.unimade.domain.qna.dto;
import lombok.*;
import org.springframework.web.multipart.MultipartFile;
import umc.unimade.domain.accounts.entity.Buyer;
import umc.unimade.domain.products.entity.Products;
import umc.unimade.domain.qna.entity.QuestionImage;
import umc.unimade.domain.qna.entity.Questions;
import umc.unimade.global.util.s3.S3Provider;
import umc.unimade.global.util.s3.dto.S3UploadRequest;

import java.util.List;
import java.util.stream.Collectors;


@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class QuestionCreateRequest {
    private String title;
    private String content;

    public Questions toEntity(Products product, Buyer buyer) {
        return Questions.builder()
                .title(this.title)
                .content(this.content)
                .product(product)
                .buyer(buyer)
                .build();
    }

    public List<QuestionImage> toQuestionImages(List<MultipartFile> images, S3Provider s3Provider, Long buyerId, Questions question) {
        if (images == null || images.isEmpty()) {
            return null;
        }
        return images.stream()
                .map(image -> {
                    String imageUrl = s3Provider.uploadFile(image,
                            S3UploadRequest.builder()
                                    .userId(buyerId)
                                    .dirName("question")
                                    .build());
                    return QuestionImage.builder()
                            .imageUrl(imageUrl)
                            .question(question)
                            .build();
                })
                .collect(Collectors.toList());
    }

}