package umc.unimade.domain.qna.service;

import lombok.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import umc.unimade.domain.accounts.entity.Buyer;
import umc.unimade.domain.accounts.entity.Seller;
import umc.unimade.domain.accounts.repository.BuyerRepository;
import umc.unimade.domain.accounts.repository.SellerRepository;
import umc.unimade.domain.products.repository.ProductRepository;
import umc.unimade.domain.products.entity.Products;
import umc.unimade.domain.qna.dto.AnswerCreateRequest;
import umc.unimade.domain.qna.dto.QuestionCreateRequest;
import umc.unimade.domain.qna.entity.AnswerImage;
import umc.unimade.domain.qna.entity.Answers;
import umc.unimade.domain.qna.entity.Questions;
import umc.unimade.domain.qna.entity.QuestionImage;
import umc.unimade.domain.qna.repository.AnswersRespository;
import umc.unimade.domain.qna.repository.QuestionsRepository;
import umc.unimade.global.common.ErrorCode;
import umc.unimade.global.common.exception.ProductsExceptionHandler;
import umc.unimade.global.common.exception.QnAExceptionHandler;
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
    private final SellerRepository sellerRepository;
    private final BuyerRepository buyerRepository;
    private final S3Provider s3Provider;
    private final AnswersRespository answersRespository;

    //To do : question에도 사진 포함하기
    @Transactional
    public void createQuestion(Long productId, Long buyerId, QuestionCreateRequest questionCreateRequest,List<MultipartFile> images) {
        Products product = findProductById(productId);
        Buyer buyer = findBuyerById(buyerId);

        Questions question = questionCreateRequest.toEntity(product, buyer);
        List<QuestionImage> questionImages = questionCreateRequest.toQuestionImages(images, s3Provider, buyerId, question);

        if (questionImages != null) {
            question.setQuestionImages(questionImages);
        }

        questionsRepository.save(question);
    }

    @Transactional
    public void createAnswer(Long questionId,Long sellerId, AnswerCreateRequest answerCreateRequest, List<MultipartFile> images){
        Questions question = findQuestionById(questionId);
        Seller seller = findSellerById(sellerId);

        Answers answer = answerCreateRequest.toEntity(seller, question);
        List<AnswerImage> answerImages = answerCreateRequest.toAnswerImages(images, s3Provider, sellerId, answer);

        if (answerImages != null) {
            answer.setAnswerImages(answerImages);
        }
        answersRespository.save(answer);
    }


    private Buyer findBuyerById(Long buyerId) {
        return buyerRepository.findById(buyerId).orElseThrow(() -> new UserExceptionHandler(ErrorCode.BUYER_NOT_FOUND));
    }
    private Seller findSellerById(Long sellerId){
        return sellerRepository.findById(sellerId).orElseThrow(() -> new UserExceptionHandler(ErrorCode.SELLER_NOT_FOUND));
    }
    private Products findProductById(Long productId){
        return productRepository.findById(productId).orElseThrow(() -> new ProductsExceptionHandler(ErrorCode.PRODUCT_NOT_FOUND));
    }
    private Questions findQuestionById(Long questionId){
        return questionsRepository.findById(questionId).orElseThrow(()-> new QnAExceptionHandler(ErrorCode.QNA_NOT_FOUND));
    }
}
