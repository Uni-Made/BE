package umc.unimade.domain.review.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import umc.unimade.domain.accounts.entity.Seller;
import umc.unimade.domain.review.dto.ReviewAnswerCreateRequest;
import umc.unimade.domain.review.dto.ReviewAnswerResposne;
import umc.unimade.domain.review.dto.ReviewReportRequest;
import umc.unimade.domain.review.dto.ReviewReportResponse;
import umc.unimade.domain.review.service.ReviewCommandService;
import umc.unimade.global.common.ApiResponse;
import umc.unimade.global.security.LoginSeller;

@RestController
@RequestMapping("/seller/review")
@Tag(name = "Review", description = "리뷰 관련 API")
@RequiredArgsConstructor
public class SellerReviewController {
    private final ReviewCommandService reviewCommandService;

    @Operation(summary = "리뷰 답변 생성")
    @PostMapping(value = "/answer/{reviewId}")
    public ApiResponse<ReviewAnswerResposne> createAnswer(@PathVariable Long reviewId,
                                                          @LoginSeller Seller seller,
                                                          @Valid @RequestBody ReviewAnswerCreateRequest reviewAnswerCreateRequest) {

        ReviewAnswerResposne reviewAnswer = reviewCommandService.createReviewAnswer(reviewId, seller, reviewAnswerCreateRequest);
        return ApiResponse.onSuccess(reviewAnswer);
    }

    @Operation(summary = "리뷰 신고하기", description = "seller가 자신의 상품에 달린 리뷰를 신고")
    @PostMapping("/{reviewId}/report")
    public ApiResponse<ReviewReportResponse> reportReview(@LoginSeller Seller seller, @PathVariable Long reviewId, @RequestBody ReviewReportRequest request) {
        ReviewReportResponse response = reviewCommandService.reportReview(reviewId, request, seller);
        return ApiResponse.onSuccess(response);
    }
}