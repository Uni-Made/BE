package umc.unimade.domain.review.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import umc.unimade.domain.accounts.entity.Buyer;
import umc.unimade.domain.accounts.exception.UserExceptionHandler;
import umc.unimade.domain.review.dto.ReviewCreateRequest;
import umc.unimade.domain.review.dto.ReviewResponse;
import umc.unimade.domain.review.service.ReviewCommandService;
import umc.unimade.domain.review.service.ReviewQueryService;
import umc.unimade.global.common.ApiResponse;
import umc.unimade.global.common.ErrorCode;
import umc.unimade.global.security.LoginBuyer;

import java.util.List;

@RestController
@RequestMapping("/buyer/review")
@Tag(name = "Review", description = "리뷰 관련 API")
@RequiredArgsConstructor
public class BuyerReviewController {
    private final ReviewCommandService reviewCommandService;
    private final ReviewQueryService reviewQueryService;

    @Operation(summary = "리뷰 생성")
    @PostMapping(value = "/{orderId}", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ApiResponse<Void> createReview(@PathVariable Long orderId,
                                          @LoginBuyer Buyer buyer,
                                          @Valid @RequestPart("reviewCreateRequest") ReviewCreateRequest reviewCreateRequest,
                                          @RequestPart(value = "reviewImages", required = false) List<MultipartFile> reviewImages) {
        try {
            reviewCommandService.createReview(orderId, buyer, reviewCreateRequest, reviewImages);
            return ApiResponse.onSuccess(null);
        } catch (IllegalArgumentException e) {
            return ApiResponse.onFailure(HttpStatus.BAD_REQUEST.name(), e.getMessage());
        } catch (UserExceptionHandler e) {
            return ApiResponse.onFailure(ErrorCode.BUYER_NOT_FOUND.getCode(), ErrorCode.BUYER_NOT_FOUND.getMessage());
        }
    }

    @Operation(summary = "리뷰 상세 내용 불러오기 ")
    @GetMapping("/{reviewId}")
    public ResponseEntity<ApiResponse<ReviewResponse>> getReview(@PathVariable Long reviewId) {
        try {
            return ResponseEntity.ok(ApiResponse.onSuccess(reviewQueryService.getReview(reviewId)));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ApiResponse.onFailure(HttpStatus.BAD_REQUEST.name(), e.getMessage()));
        } catch (UserExceptionHandler e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(ApiResponse.onFailure(ErrorCode.BUYER_NOT_FOUND.getCode(), ErrorCode.BUYER_NOT_FOUND.getMessage()));
        }
    }

    @Operation(summary = "리뷰 삭제하기")
    @DeleteMapping("/{reviewId}")
    public ResponseEntity<ApiResponse<Void>> deleteReview(@PathVariable Long reviewId,
                                                          @LoginBuyer Buyer buyer) {
        try {
            reviewCommandService.deleteReview(reviewId, buyer);
            return ResponseEntity.ok(ApiResponse.onSuccess(null));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ApiResponse.onFailure(HttpStatus.BAD_REQUEST.name(), e.getMessage()));
        } catch (UserExceptionHandler e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(ApiResponse.onFailure(ErrorCode.REVIEW_NOT_FOUND.getCode(), ErrorCode.REVIEW_NOT_FOUND.getMessage()));
        }
    }
}
