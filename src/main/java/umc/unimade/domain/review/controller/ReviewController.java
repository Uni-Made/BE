package umc.unimade.domain.review.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.*;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import umc.unimade.domain.accounts.entity.Buyer;
import umc.unimade.domain.review.dto.ReviewReportRequest;
import umc.unimade.domain.review.dto.ReviewReportResponse;
import umc.unimade.domain.review.dto.ReviewCreateRequest;
import umc.unimade.domain.review.dto.ReviewResponse;
import umc.unimade.domain.review.service.ReviewCommandService;
import umc.unimade.domain.review.service.ReviewQueryService;
import umc.unimade.global.common.ApiResponse;
import umc.unimade.global.common.ErrorCode;
import umc.unimade.domain.accounts.exception.UserExceptionHandler;
import umc.unimade.global.security.LoginBuyer;

import java.util.List;

@RestController
@RequestMapping("/api/review")
@Tag(name = "Review", description = "리뷰 관련 API")
@RequiredArgsConstructor
public class ReviewController {
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

    @Operation(summary = "리뷰 삭제하기", description = "buyerId 나중에 뺄게요!  ")
    @DeleteMapping("/{reviewId}")
    public ResponseEntity<ApiResponse<Void>> deleteReview(@PathVariable Long reviewId,
                                                          @LoginBuyer Buyer buyer) {
        try {
            reviewCommandService.deleteReview(reviewId,buyer);
            return ResponseEntity.ok(ApiResponse.onSuccess(null));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ApiResponse.onFailure(HttpStatus.BAD_REQUEST.name(), e.getMessage()));
        } catch (UserExceptionHandler e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(ApiResponse.onFailure(ErrorCode.REVIEW_NOT_FOUND.getCode(), ErrorCode.REVIEW_NOT_FOUND.getMessage()));
        }
    }


    // TODO: seller 받아 오기
    @Operation(summary = "리뷰 신고하기", description = "seller가 자신의 상품에 달린 리뷰를 신고")
    @PostMapping("/{reviewId}/report")
    public ApiResponse<ReviewReportResponse> reportReview(@PathVariable Long reviewId, @RequestBody ReviewReportRequest request) {
        ReviewReportResponse response = reviewCommandService.reportReview(reviewId, request);
        return ApiResponse.onSuccess(response);
    }
}


