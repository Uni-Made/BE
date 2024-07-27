package umc.unimade.domain.qna.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.*;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import umc.unimade.domain.accounts.entity.Buyer;
import umc.unimade.domain.accounts.entity.Seller;
import umc.unimade.domain.qna.dto.AnswerCreateRequest;
import umc.unimade.domain.qna.dto.AnswerResponse;
import umc.unimade.domain.qna.dto.QuestionCreateRequest;
import umc.unimade.domain.qna.dto.QuestionResponse;
import umc.unimade.domain.qna.service.QnACommandService;
import umc.unimade.domain.qna.service.QnAQueryService;
import umc.unimade.global.common.ApiResponse;
import umc.unimade.global.common.ErrorCode;
import umc.unimade.domain.accounts.exception.UserExceptionHandler;

import java.util.List;

@RestController
@RequestMapping("/api/qna")
@RequiredArgsConstructor
public class QnAController {
    private final QnACommandService qnaCommandService;
    private final QnAQueryService qnaQueryService;

    @Tag(name = "QnA", description = "qna 관련 API")
    @Operation(summary = "질문 생성")
    @PostMapping(value = "/question/{productId}")
    public ResponseEntity<ApiResponse<Void>> createQuestion(@AuthenticationPrincipal Buyer currentBuyer,
                                                            @PathVariable Long productId,
                                                            @RequestPart("questionCreateRequest") QuestionCreateRequest questionCreateRequest) {
        try {
            qnaCommandService.createQuestion(productId, currentBuyer, questionCreateRequest);
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
    @PostMapping(value = "/answer/{questionId}")
    public ResponseEntity<ApiResponse<Void>> createAnswer(@PathVariable Long questionId, @AuthenticationPrincipal Seller currentSeller, @RequestPart("answerCreateRequest") AnswerCreateRequest answerCreateRequest) {
        try {
            qnaCommandService.createAnswer(questionId, currentSeller, answerCreateRequest);
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

}
