package umc.unimade.domain.products.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.*;
import umc.unimade.domain.accounts.dto.AdminRegisterRequest;
import umc.unimade.domain.products.dto.AdminProductRegisterResponse;
import umc.unimade.domain.products.dto.ProductRegisterResponse;
import umc.unimade.domain.products.dto.ProductRegistersResponse;
import umc.unimade.domain.products.entity.RegisterType;
import umc.unimade.domain.products.service.ProductRegisterCommandService;
import umc.unimade.domain.products.service.ProductRegisterQueryService;
import umc.unimade.global.common.ApiResponse;
import umc.unimade.global.registerStatus.RegisterStatus;

@RequiredArgsConstructor
@Tag(name = "Admin_Product", description = "관리자-상품 관련 API")
@RequestMapping("/admin/product")
@RestController
public class ProductRegisterController {

    private final ProductRegisterCommandService productRegisterCommandService;
    private final ProductRegisterQueryService productRegisterQueryService;

    @Operation(summary = "상품 등록 요청 승인", description = "유저 role이 관리자인 사람만 가능, 요청 상태가 PENDING or HOLD인 것만 승인 가능")
    @PostMapping("/register/{productRegisterId}/approve")
    public ApiResponse<AdminProductRegisterResponse> approveProduct(@PathVariable(name = "productRegisterId") Long productRegisterId) {
        return ApiResponse.onSuccess(productRegisterCommandService.approveProduct(productRegisterId, RegisterStatus.ACCEPTED));
    }

    @Operation(summary = "상품 등록 요청 거절", description = "유저 role이 관리자인 사람만 가능, 요청 상태가 PENDING인 것만 거절 가능")
    @PostMapping("/register/{productRegisterId}/reject")
    public ApiResponse<AdminProductRegisterResponse> rejectProduct(@PathVariable(name = "productRegisterId") Long productRegisterId,
                                                                 @RequestBody AdminRegisterRequest request) {
        return ApiResponse.onSuccess(productRegisterCommandService.rejectOrHoldProduct(productRegisterId, RegisterStatus.REJECTED, request));
    }

    @Operation(summary = "상품 등록 요청 보류", description = "유저 role이 관리자인 사람만 가능, 요청 상태가 PENDING인 것만 보류 가능")
    @PostMapping("/register/{productRegisterId}/hold")
    public ApiResponse<AdminProductRegisterResponse> holdProduct(@PathVariable(name = "productRegisterId") Long productRegisterId,
                                                               @RequestBody AdminRegisterRequest request) {
        return ApiResponse.onSuccess(productRegisterCommandService.rejectOrHoldProduct(productRegisterId, RegisterStatus.HOLD, request));
    }

    @Operation(summary = "상품 수정 요청 승인", description = "유저 role이 관리자인 사람만 가능, 요청 상태가 PENDING or HOLD인 것만 승인 가능")
    @PostMapping("/update/{productRegisterId}/approve")
    public ApiResponse<AdminProductRegisterResponse> approveUpdateProduct(@PathVariable(name = "productRegisterId") Long productRegisterId) {
        return ApiResponse.onSuccess(productRegisterCommandService.approveUpdateProduct(productRegisterId, RegisterStatus.ACCEPTED));
    }

    @Operation(summary = "상품 수정 요청 거절", description = "유저 role이 관리자인 사람만 가능, 요청 상태가 PENDING인 것만 거절 가능")
    @PostMapping("/update/{productRegisterId}/reject")
    public ApiResponse<AdminProductRegisterResponse> rejectUpdateProduct(@PathVariable(name = "productRegisterId") Long productRegisterId,
                                                                   @RequestBody AdminRegisterRequest request) {
        return ApiResponse.onSuccess(productRegisterCommandService.rejectOrHoldProduct(productRegisterId, RegisterStatus.REJECTED, request));
    }

    @Operation(summary = "상품 수정 요청 보류", description = "유저 role이 관리자인 사람만 가능, 요청 상태가 PENDING인 것만 보류 가능")
    @PostMapping("/update/{productRegisterId}/hold")
    public ApiResponse<AdminProductRegisterResponse> holdUpdateProduct(@PathVariable(name = "productRegisterId") Long productRegisterId,
                                                                 @RequestBody AdminRegisterRequest request) {
        return ApiResponse.onSuccess(productRegisterCommandService.rejectOrHoldProduct(productRegisterId, RegisterStatus.HOLD, request));
    }

    @Operation(summary = "전체 상품 등록 요청 목록 조회",
            description = "유저 role이 관리자인 사람만 가능, 상태값 파라미터로 아무것도 안넣으면 전체 조회, 상태값에 따라 조회도 가능")
    @GetMapping("/products")
    public ApiResponse<Page<ProductRegistersResponse>> getProductsRegisters(
            @RequestParam(name = "page", defaultValue = "0") int page,
            @RequestParam(name = "size", defaultValue = "10") int size,
            @RequestParam(name = "status", required = false) RegisterStatus status,
            @RequestParam(name = "type", required = false) RegisterType type) {
        Pageable pageable = PageRequest.of(page, size);
        Page<ProductRegistersResponse> responses = productRegisterQueryService.getProductsRegisters(pageable, status, type);
        return ApiResponse.onSuccess(responses);
    }

    @Operation(summary = "상품 등록 요청 개별 조회", description = "등록 요청한 상품 정보 조회")
    @GetMapping("/{productRegisterId}")
    public ApiResponse<ProductRegisterResponse> getProductRegister(@PathVariable(name = "productRegisterId") Long productRegisterId) {
        ProductRegisterResponse productRegister = productRegisterQueryService.getProductRegister(productRegisterId);
        return ApiResponse.onSuccess(productRegister);
    }
}
