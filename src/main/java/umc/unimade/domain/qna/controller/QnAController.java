package umc.unimade.domain.qna.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.*;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import umc.unimade.domain.accounts.entity.Buyer;
import umc.unimade.domain.accounts.entity.Seller;
import umc.unimade.domain.qna.dto.AnswerCreateRequest;
import umc.unimade.domain.qna.dto.QuestionCreateRequest;
import umc.unimade.domain.qna.service.QnACommandService;
import umc.unimade.global.common.ApiResponse;
import umc.unimade.global.common.ErrorCode;
import umc.unimade.domain.accounts.exception.UserExceptionHandler;
import umc.unimade.global.security.LoginBuyer;
import umc.unimade.global.security.LoginSeller;

@RestController
@RequestMapping("/qna")
@RequiredArgsConstructor
public class QnAController {
    private final QnACommandService qnaCommandService;

    @Tag(name = "QnA", description = "qna 관련 API")
    @Operation(summary = "질문 작성")
    @PostMapping(value = "/question/{productId}")
    public ResponseEntity<ApiResponse<Void>> createQuestion(@PathVariable Long productId,
                                                            @LoginBuyer Buyer buyer,
                                                            @Valid @RequestBody QuestionCreateRequest questionCreateRequest) {
        try {
            qnaCommandService.createQuestion(productId, buyer, questionCreateRequest);
            return ResponseEntity.ok(ApiResponse.onSuccess(null));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ApiResponse.onFailure(HttpStatus.BAD_REQUEST.name(), e.getMessage()));
        } catch (UserExceptionHandler e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(ApiResponse.onFailure(ErrorCode.BUYER_NOT_FOUND.getCode(), ErrorCode.BUYER_NOT_FOUND.getMessage()));
        }
    }


    @Tag(name = "QnA", description = "qna 관련 API")
    @Operation(summary = "질문 삭제")
    @DeleteMapping(value = "/question/{questionId}")
    public ResponseEntity<ApiResponse<Void>> deleteQuestion(@PathVariable Long questionId,
                                                            @LoginBuyer Buyer buyer) {
        try {
            qnaCommandService.deleteQuestion(questionId, buyer);
            return ResponseEntity.ok(ApiResponse.onSuccess(null));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ApiResponse.onFailure(HttpStatus.BAD_REQUEST.name(), e.getMessage()));
        } catch (UserExceptionHandler e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(ApiResponse.onFailure(ErrorCode.BUYER_NOT_FOUND.getCode(), ErrorCode.BUYER_NOT_FOUND.getMessage()));
        }
    }

    @Tag(name = "QnA", description = "qna 관련 API")
    @Operation(summary = "답변 생성")
    @PostMapping(value = "/answer/{questionId}")
    public ResponseEntity<ApiResponse<Void>> createAnswer(@PathVariable Long questionId,
                                                          @LoginSeller Seller seller,
                                                          @Valid @RequestBody AnswerCreateRequest answerCreateRequest) {
        try {
            qnaCommandService.createAnswer(questionId, seller, answerCreateRequest);
            return ResponseEntity.ok(ApiResponse.onSuccess(null));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ApiResponse.onFailure(HttpStatus.BAD_REQUEST.name(), e.getMessage()));
        } catch (UserExceptionHandler e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(ApiResponse.onFailure(ErrorCode.SELLER_NOT_FOUND.getCode(), ErrorCode.SELLER_NOT_FOUND.getMessage()));
        }
    }


    @Tag(name = "QnA", description = "qna 관련 API")
    @Operation(summary = "답변 삭제")
    @DeleteMapping(value = "/answer/{answerId}")
    public ResponseEntity<ApiResponse<Void>> deleteAnswer(@PathVariable Long answerId,
                                                          @LoginSeller Seller seller) {
        try {
            qnaCommandService.deleteAnswer(answerId, seller);
            return ResponseEntity.ok(ApiResponse.onSuccess(null));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ApiResponse.onFailure(HttpStatus.BAD_REQUEST.name(), e.getMessage()));
        } catch (UserExceptionHandler e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(ApiResponse.onFailure(ErrorCode.SELLER_NOT_FOUND.getCode(), ErrorCode.SELLER_NOT_FOUND.getMessage()));
        }
    }
}
