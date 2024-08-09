package umc.unimade.domain.review.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.web.bind.annotation.*;
import org.springframework.data.domain.Pageable;
import umc.unimade.domain.review.dto.ReportProcessResponse;
import umc.unimade.domain.review.dto.ReviewReportResponse;
import umc.unimade.domain.review.dto.ReviewReportsResponse;
import umc.unimade.domain.review.entity.ReportStatus;
import umc.unimade.domain.review.service.ReviewReportCommandService;
import umc.unimade.domain.review.service.ReviewReportQueryService;
import umc.unimade.global.common.ApiResponse;

@RestController
@RequestMapping("/admin/report")
@Tag(name = "Admin_report", description = "관리자-리뷰 신고 관련 API")
@RequiredArgsConstructor
public class AdminReviewController {

    private final ReviewReportQueryService reviewReportQueryService;
    private final ReviewReportCommandService reviewReportCommandService;

    @Operation(summary = "리뷰 신고 목록 조회", description = "유저 role이 관리자인 사람만 가능")
    @GetMapping()
    public ApiResponse<Page<ReviewReportsResponse>> getReports(
            @RequestParam(name = "page", defaultValue = "0") int page,
            @RequestParam(name = "size", defaultValue = "10") int size) {
        Pageable pageable = PageRequest.of(page, size, Sort.by(Sort.Direction.DESC, "createdAt"));
        Page<ReviewReportsResponse> responses = reviewReportQueryService.getReports(pageable);
        return ApiResponse.onSuccess(responses);
    }

    @Operation(summary = "리뷰 신고 개별 조회", description = "유저 role이 관리자인 사람만 가능")
    @GetMapping("/{reviewReportId}")
    public ApiResponse<ReviewReportResponse> getReport(@PathVariable Long reviewReportId) {
        ReviewReportResponse response = reviewReportQueryService.getReport(reviewReportId);
        return ApiResponse.onSuccess(response);
    }

    @Operation(summary = "리뷰 신고 처리", description = "유저 role이 관리자인 사람만 가능, 3일, 일주일, 한달, 영구 정지")
    @PostMapping("/{reviewReportId}")
    public ApiResponse<ReportProcessResponse> processReport(@PathVariable Long reviewReportId,
                                                            @RequestParam ReportStatus reportStatus) {
        ReportProcessResponse response = reviewReportCommandService.processReport(reviewReportId, reportStatus);
        return ApiResponse.onSuccess(response);
    }

}
