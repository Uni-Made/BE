package umc.unimade.domain.review.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.*;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import umc.unimade.domain.accounts.entity.Buyer;
import umc.unimade.domain.review.dto.ReviewCreateRequest;
import umc.unimade.domain.review.dto.ReviewResponse;
import umc.unimade.domain.review.service.ReviewCommandService;
import umc.unimade.domain.review.service.ReviewQueryService;
import umc.unimade.global.common.ApiResponse;
import umc.unimade.global.common.ErrorCode;
import umc.unimade.domain.accounts.exception.UserExceptionHandler;

import java.util.List;

@RestController
@RequestMapping("/api/review")
@RequiredArgsConstructor
public class ReviewController {
    private final ReviewCommandService reviewCommandService;
    private final ReviewQueryService reviewQueryService;

    @Tag(name = "Review", description = "리뷰 관련 API")
    @Operation(summary = "리뷰 생성")
    @PostMapping(value = "/{productId}", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<ApiResponse<Void>> createReview(@PathVariable Long productId,
                                                          @AuthenticationPrincipal Buyer currentBuyer,
                                                          @RequestPart("reviewCreateRequest") ReviewCreateRequest reviewCreateRequest,
                                                          @RequestPart(value = "reviewImages", required = false) List<MultipartFile> reviewImages) {
        try {
            reviewCommandService.createReview(productId, currentBuyer, reviewCreateRequest, reviewImages);
            return ResponseEntity.ok(ApiResponse.onSuccess(null));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ApiResponse.onFailure(HttpStatus.BAD_REQUEST.name(), e.getMessage()));
        } catch (UserExceptionHandler e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(ApiResponse.onFailure(ErrorCode.BUYER_NOT_FOUND.getCode(), ErrorCode.BUYER_NOT_FOUND.getMessage()));
        }
    }

        @Tag(name = "Review", description = "리뷰 관련 API")
        @Operation(summary = "리뷰 상세 내용 불러오기 ")
        @GetMapping("/{reviewId}")
        //To do : user 토큰 추가
        public ResponseEntity<ApiResponse<ReviewResponse>> getReview(@PathVariable Long reviewId) {
            try {
                return ResponseEntity.ok(ApiResponse.onSuccess(reviewQueryService.getReview(reviewId)));
            } catch (IllegalArgumentException e) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ApiResponse.onFailure(HttpStatus.BAD_REQUEST.name(), e.getMessage()));
            } catch (UserExceptionHandler e) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(ApiResponse.onFailure(ErrorCode.BUYER_NOT_FOUND.getCode(), ErrorCode.BUYER_NOT_FOUND.getMessage()));
            }
    }
}


