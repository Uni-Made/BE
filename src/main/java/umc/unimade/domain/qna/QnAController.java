package umc.unimade.domain.qna;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.*;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import umc.unimade.domain.qna.dto.QuestionCreateRequest;
import umc.unimade.domain.qna.dto.QuestionResponse;
import umc.unimade.domain.qna.service.QnACommandService;
import umc.unimade.domain.qna.service.QnAQueryService;
import umc.unimade.global.common.ApiResponse;
import umc.unimade.global.common.ErrorCode;
import umc.unimade.global.common.exception.UserExceptionHandler;

import java.util.List;

@RestController
@RequestMapping("/api/qna")
@RequiredArgsConstructor
public class QnAController {
    private final QnACommandService qnaCommandService;
    private final QnAQueryService qnaQueryService;

    //To do : QnA DIR 생성
    @Tag(name = "QnA", description = "qna 관련 API")
    @Operation(summary = "질문 생성")
    @PostMapping(value = "/{productId}/{buyerId}", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<ApiResponse<Void>> createReview(@PathVariable Long productId, @PathVariable Long buyerId, @RequestPart("questionCreateRequest") QuestionCreateRequest questionCreateRequest, @RequestPart(value = "questionImages", required = false) List<MultipartFile> questionImages) {
        try {
            qnaCommandService.createQuestion(productId, buyerId, questionCreateRequest, questionImages);
            return ResponseEntity.ok(ApiResponse.onSuccess(null));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ApiResponse.onFailure(HttpStatus.BAD_REQUEST.name(), e.getMessage()));
        } catch (UserExceptionHandler e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(ApiResponse.onFailure(ErrorCode.BUYER_NOT_FOUND.getCode(), ErrorCode.BUYER_NOT_FOUND.getMessage()));
        }
    }

    @Tag(name = "QnA", description = "qna 관련 API")
    @Operation(summary = "질문 불러오기")
    @GetMapping("/{questionId}")
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

}
