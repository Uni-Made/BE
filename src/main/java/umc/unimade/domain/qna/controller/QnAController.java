package umc.unimade.domain.qna.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.*;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import umc.unimade.domain.accounts.entity.Buyer;
import umc.unimade.domain.accounts.entity.Seller;
import umc.unimade.domain.accounts.repository.BuyerRepository;
import umc.unimade.domain.accounts.repository.SellerRepository;
import umc.unimade.domain.qna.dto.AnswerCreateRequest;
import umc.unimade.domain.qna.dto.AnswerResponse;
import umc.unimade.domain.qna.dto.QuestionCreateRequest;
import umc.unimade.domain.qna.dto.QuestionResponse;
import umc.unimade.domain.qna.service.QnACommandService;
import umc.unimade.domain.qna.service.QnAQueryService;
import umc.unimade.global.common.ApiResponse;
import umc.unimade.global.common.ErrorCode;
import umc.unimade.domain.accounts.exception.UserExceptionHandler;


@RestController
@RequestMapping("/api/qna")
@RequiredArgsConstructor
public class QnAController {
    private final QnACommandService qnaCommandService;
    private final QnAQueryService qnaQueryService;
    private final BuyerRepository buyerRepository;
    private final SellerRepository sellerRepository;

    @Tag(name = "QnA", description = "qna 관련 API")
    @Operation(summary = "질문 생성")
    @PostMapping(value = "/question/{buyerId}/{productId}")
    public ResponseEntity<ApiResponse<Void>> createQuestion(@PathVariable Long buyerId,
                                                            @PathVariable Long productId,
                                                            @RequestBody QuestionCreateRequest questionCreateRequest) {
        try {
            Buyer buyer = findBuyerById(buyerId);
            qnaCommandService.createQuestion(productId, buyer, questionCreateRequest);
            return ResponseEntity.ok(ApiResponse.onSuccess(null));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ApiResponse.onFailure(HttpStatus.BAD_REQUEST.name(), e.getMessage()));
        } catch (UserExceptionHandler e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(ApiResponse.onFailure(ErrorCode.BUYER_NOT_FOUND.getCode(), ErrorCode.BUYER_NOT_FOUND.getMessage()));
        }
    }

    @Tag(name = "QnA", description = "qna 관련 API")
    @Operation(summary = "질문 불러오기")
    @GetMapping("/question/{questionId}")
    //To do : user 토큰 추가
    public ResponseEntity<ApiResponse<QuestionResponse>> getQuestion(@PathVariable Long questionId) {
        try {
            return ResponseEntity.ok(ApiResponse.onSuccess(qnaQueryService.getQuestion(questionId)));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ApiResponse.onFailure(HttpStatus.BAD_REQUEST.name(), e.getMessage()));
        } catch (UserExceptionHandler e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(ApiResponse.onFailure(ErrorCode.BUYER_NOT_FOUND.getCode(), ErrorCode.BUYER_NOT_FOUND.getMessage()));
        }
    }

    @Tag(name = "QnA", description = "qna 관련 API")
    @Operation(summary = "답변 생성")
    @PostMapping(value = "/answer/{sellerId}/{questionId}")
    public ResponseEntity<ApiResponse<Void>> createAnswer(@PathVariable Long questionId,
                                                          @PathVariable Long sellerId,
                                                          @RequestBody AnswerCreateRequest answerCreateRequest) {
        try {
            Seller currentSeller = findSellerById(sellerId);
            return ResponseEntity.ok(ApiResponse.onSuccess(null));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ApiResponse.onFailure(HttpStatus.BAD_REQUEST.name(), e.getMessage()));
        } catch (UserExceptionHandler e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(ApiResponse.onFailure(ErrorCode.SELLER_NOT_FOUND.getCode(), ErrorCode.SELLER_NOT_FOUND.getMessage()));
        }
    }

    @Tag(name = "QnA", description = "qna 관련 API")
    @Operation(summary = "답변 불러오기")
    @GetMapping("/answer/{answerId}")
    //To do : user 토큰 추가
    public ResponseEntity<ApiResponse<AnswerResponse>> getAnswer(@PathVariable Long answerId) {
        try {
            return ResponseEntity.ok(ApiResponse.onSuccess(qnaQueryService.getAnswer(answerId)));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ApiResponse.onFailure(HttpStatus.BAD_REQUEST.name(), e.getMessage()));
        } catch (UserExceptionHandler e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(ApiResponse.onFailure(ErrorCode.SELLER_NOT_FOUND.getCode(), ErrorCode.SELLER_NOT_FOUND.getMessage()));
        }

    }

    // 임시로 추가
    private Buyer findBuyerById(Long buyerId) {
        return buyerRepository.findById(buyerId)
                .orElseThrow(() -> new UserExceptionHandler(ErrorCode.BUYER_NOT_FOUND));
    }

    private Seller findSellerById(Long sellerId){
        return sellerRepository.findById(sellerId)
                .orElseThrow(() -> new UserExceptionHandler(ErrorCode.SELLER_NOT_FOUND));
    }

}
