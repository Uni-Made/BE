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
import umc.unimade.domain.products.exception.ProductsExceptionHandler;
import umc.unimade.domain.qna.exception.QnAExceptionHandler;
import umc.unimade.domain.accounts.exception.UserExceptionHandler;
import umc.unimade.global.util.s3.S3Provider;

import java.util.List;

@Service
@RequiredArgsConstructor
public class QnACommandService {
    private final QuestionsRepository questionsRepository;
    private final ProductRepository productRepository;
    private final SellerRepository sellerRepository;
    private final BuyerRepository buyerRepository;
    private final S3Provider s3Provider;
    private final AnswersRespository answersRespository;

    @Transactional
    public void createQuestion(Long productId, Buyer buyer, QuestionCreateRequest questionCreateRequest) {
        Products product = findProductById(productId);
        Questions question = questionCreateRequest.toEntity(product, buyer);
        questionsRepository.save(question);
    }

    @Transactional
    public void createAnswer(Long questionId, Seller seller , AnswerCreateRequest answerCreateRequest){
        Questions question = findQuestionById(questionId);
        Answers answer = answerCreateRequest.toEntity(seller, question);
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
