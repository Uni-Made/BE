package umc.unimade.domain.qna.service;

import lombok.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import umc.unimade.domain.accounts.entity.Buyer;
import umc.unimade.domain.accounts.repository.BuyerRepository;
import umc.unimade.domain.products.ProductRepository;
import umc.unimade.domain.products.entity.Products;
import umc.unimade.domain.qna.dto.QuestionCreateRequest;
import umc.unimade.domain.qna.entity.Questions;
import umc.unimade.domain.qna.entity.QuestionImage;
import umc.unimade.domain.qna.repository.QuestionsRepository;
import umc.unimade.global.common.ErrorCode;
import umc.unimade.global.common.exception.ProductsExceptionHandler;
import umc.unimade.global.common.exception.UserExceptionHandler;
import umc.unimade.global.util.s3.S3Provider;
import umc.unimade.global.util.s3.dto.S3UploadRequest;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class QnACommandService {
    private final QuestionsRepository questionsRepository;
    private final ProductRepository productRepository;
    private final BuyerRepository buyerRepository;
    private final S3Provider s3Provider;

    //To do : question에도 사진 포함하기
    @Transactional
    public void createQuestion(Long productId, Long buyerId, QuestionCreateRequest questionCreateRequest,List<MultipartFile> images) {
        Products product = findProductById(productId);
        Buyer buyer = findBuyerById(buyerId);

        Questions question = Questions.builder()
                .title(questionCreateRequest.getTitle())
                .content(questionCreateRequest.getContent())
                .product(product)
                .buyer(buyer)
                .build();

        if (images != null && !images.isEmpty()) {
            List<QuestionImage> questionImages = images.stream()
                    .map(image -> {
                        String imageUrl = s3Provider.uploadFile(image,
                                S3UploadRequest.builder()
                                        .userId(buyerId)
                                        //To do : 디렉토리 생성하면 dirName "question"으로 수정하기
                                        .dirName("review")
                                        .build());
                        return QuestionImage.builder()
                                .imageUrl(imageUrl)
                                .question(question)
                                .build();
                    })
                    .collect(Collectors.toList());
            question.setQuestionImages(questionImages);
        }
        questionsRepository.save(question);


    }


    private Buyer findBuyerById(Long buyerId) {
        return buyerRepository.findById(buyerId).orElseThrow(() -> new UserExceptionHandler(ErrorCode.BUYER_NOT_FOUND));
    }
    private Products findProductById(Long productId){
        return productRepository.findById(productId).orElseThrow(() -> new ProductsExceptionHandler(ErrorCode.PRODUCT_NOT_FOUND));
    }
}
