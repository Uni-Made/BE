package umc.unimade.domain.products.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import umc.unimade.domain.accounts.entity.Seller;
import umc.unimade.domain.products.dto.ProductRegisterResponse;
import umc.unimade.domain.products.dto.ProductRequest;
import umc.unimade.domain.products.dto.ProductResponse;
import umc.unimade.domain.products.dto.ProductUpdateResponse;
import umc.unimade.domain.products.service.ProductsCommandService;
import umc.unimade.global.common.ApiResponse;
import umc.unimade.global.common.BaseEntity;
import umc.unimade.global.security.LoginSeller;

import java.util.List;

@RestController
@RequestMapping("/seller/product")
@RequiredArgsConstructor
public class SellerProductController extends BaseEntity {
    private final ProductsCommandService productsCommandService;

    // 상품 등록
    @Tag(name = "Products")
    @Operation(summary = "상품 등록", description = "productRegister로 저장")
    @PostMapping(value = "/create", consumes = {MediaType.MULTIPART_FORM_DATA_VALUE})
    public ResponseEntity<ApiResponse<ProductRegisterResponse>> createProduct(@LoginSeller Seller seller,
                                                                              @RequestPart("createProductDto") ProductRequest.CreateProductDto request,
                                                                              @RequestPart(name = "image", required = false) List<MultipartFile> images,
                                                                              @RequestPart(name = "detailImage", required = false) List<MultipartFile> detailImages) {
        ApiResponse<ProductRegisterResponse> createdProduct = productsCommandService.createProduct(seller, request, images, detailImages);
        return new ResponseEntity<>(createdProduct, HttpStatus.CREATED);
    }

    // 상품 수정
    @Tag(name = "Products")
    @Operation(summary = "상품 수정")
    @PatchMapping(value = "/{productId}", consumes = {MediaType.MULTIPART_FORM_DATA_VALUE})
    public ResponseEntity<ApiResponse<ProductUpdateResponse>> updateProduct(@LoginSeller Seller seller,
                                                                            @PathVariable Long productId,
                                                                            @RequestPart("updateProductDto") ProductRequest.UpdateProductDto request,
                                                                            @RequestPart(name = "image", required = false) List<MultipartFile> images,
                                                                            @RequestPart(name = "detailImage", required = false) List<MultipartFile> detailImages) {
        ApiResponse<ProductUpdateResponse> updatedProduct = productsCommandService.updateProduct(seller, productId, request, images, detailImages);
        return ResponseEntity.ok(updatedProduct);
    }

    // 상품 삭제
    @Tag(name = "Products")
    @Operation(summary = "상품 삭제")
    @DeleteMapping("/{productId}")
    public ApiResponse<Object> deleteProduct(@PathVariable Long productId) {
        productsCommandService.deleteProduct(productId);
        return ApiResponse.noContent();
    }

    // 판매 재등록
    @Tag(name = "Products")
    @Operation(summary = "판매 재등록 (판매 종료 상품 판매 재등록)")
    @PutMapping("/{productId}/resale")
    public ResponseEntity<ApiResponse<ProductResponse>> resaleProduct(@PathVariable Long productId) {
        ApiResponse<ProductResponse> response = productsCommandService.resaleProduct(productId);
        return ResponseEntity.ok(response);
    }
}
