package umc.unimade.domain.accounts.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.*;
import umc.unimade.domain.accounts.dto.SellerRegisterResponse;
import umc.unimade.domain.accounts.dto.SellerRegistersResponse;
import umc.unimade.domain.accounts.service.SellerRegisterQueryService;
import umc.unimade.domain.accounts.dto.AdminSellerRegisterRequest;
import umc.unimade.domain.accounts.dto.AdminSellerRegisterResponse;
import umc.unimade.domain.accounts.service.SellerRegisterCommandService;
import umc.unimade.global.common.ApiResponse;
import umc.unimade.global.registerStatus.RegisterStatus;

@RequiredArgsConstructor
@Tag(name = "Admin", description = "관리자 관련 API")
@RequestMapping("/admin")
@RestController
public class AdminController {  // TODO: 필터체인에 /admin으로 접근시 ROlE이 ADMIN인지 확인 넣기

    private final SellerRegisterCommandService sellerRegisterCommandService;
    private final SellerRegisterQueryService sellerRegisterQueryService;

    @Operation(summary = "판매자의 회원가입 요청 승인", description = "유저 role이 관리자인 사람만 가능, 요청 상태가 PENDING or HOLD인 것만 승인 가능")
    @PostMapping("/{sellerRegisterId}/approve")
    public ApiResponse<AdminSellerRegisterResponse> approveSeller(@PathVariable(name = "sellerRegisterId") Long sellerRegisterId) {
        return ApiResponse.onSuccess(sellerRegisterCommandService.approveSeller(sellerRegisterId, RegisterStatus.ACCEPTED));
    }

    @Operation(summary = "판매자의 회원가입 요청 거절", description = "유저 role이 관리자인 사람만 가능, 요청 상태가 PENDING인 것만 거절 가능")
    @PostMapping("/{sellerRegisterId}/reject")
    public ApiResponse<AdminSellerRegisterResponse> rejectSeller(@PathVariable(name = "sellerRegisterId") Long sellerRegisterId,
                                                                 @RequestBody AdminSellerRegisterRequest request) {
        return ApiResponse.onSuccess(sellerRegisterCommandService.rejectOrHoldSeller(sellerRegisterId, RegisterStatus.REJECTED, request));
    }

    @Operation(summary = "판매자의 회원가입 요청 보류", description = "유저 role이 관리자인 사람만 가능, 요청 상태가 PENDING인 것만 보류 가능")
    @PostMapping("/{sellerRegisterId}/hold")
    public ApiResponse<AdminSellerRegisterResponse> holdSeller(@PathVariable(name = "sellerRegisterId") Long sellerRegisterId,
                                                               @RequestBody AdminSellerRegisterRequest request) {
        return ApiResponse.onSuccess(sellerRegisterCommandService.rejectOrHoldSeller(sellerRegisterId, RegisterStatus.HOLD, request));
    }

    @Operation(summary = "전체 판매자의 회원가입 요청 목록 조회",
            description = "유저 role이 관리자인 사람만 가능, 상태값 파라미터로 아무것도 안넣으면 전체 조회, 상태값에 따라 조회도 가능")
    @GetMapping("/sellers")
    public ApiResponse<Page<SellerRegistersResponse>> getSellersRegisters(
            @RequestParam(name = "page", defaultValue = "0") int page,
            @RequestParam(name = "size", defaultValue = "10") int size,
            @RequestParam(name = "status", required = false) RegisterStatus status) {
        Pageable pageable = PageRequest.of(page, size);
        Page<SellerRegistersResponse> responses = sellerRegisterQueryService.getSellersRegisters(pageable, status);
        return ApiResponse.onSuccess(responses);
    }

    @Operation(summary = "판매자의 회원가입 요청 개별 조회", description = "요청한 판매자의 정보 조회")
    @GetMapping("{sellerRegisterId}")
    public ApiResponse<SellerRegisterResponse> getSellerRegister(@PathVariable(name = "sellerRegisterId") Long sellerRegisterId) {
        SellerRegisterResponse sellerRegister = sellerRegisterQueryService.getSellerRegister(sellerRegisterId);
        return ApiResponse.onSuccess(sellerRegister);
    }

}
