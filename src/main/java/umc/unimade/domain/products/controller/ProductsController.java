package umc.unimade.domain.products.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import lombok.*;
import org.springframework.web.multipart.MultipartFile;
import umc.unimade.domain.accounts.entity.Buyer;
import umc.unimade.domain.products.dto.ProductRegisterResponse;
import umc.unimade.domain.products.dto.ProductRequest.UpdateProductDto;
import umc.unimade.domain.products.dto.ProductResponse;
import umc.unimade.domain.products.dto.ProductRequest.CreateProductDto;
import umc.unimade.domain.products.dto.ProductUpdateResponse;
import umc.unimade.domain.products.dto.ProductsListResponse;
import umc.unimade.domain.products.entity.ViewType;
import umc.unimade.domain.products.service.ProductsCommandService;
import umc.unimade.domain.products.service.ProductsQueryService;
import umc.unimade.global.common.ApiResponse;
import umc.unimade.global.common.BaseEntity;
import umc.unimade.global.common.ErrorCode;
import umc.unimade.domain.products.exception.ProductsExceptionHandler;
import umc.unimade.domain.accounts.exception.UserExceptionHandler;

import java.util.List;

@RestController
@RequestMapping("/api/products")
@RequiredArgsConstructor
public class ProductsController extends BaseEntity {
    private final ProductsQueryService productsQueryService;
    private final ProductsCommandService productsCommandService;

    @Tag(name = "Products", description = "판매 상품 관련 API")
    @Operation(summary = "판매 상품 정보 가져오기")
    @GetMapping("/{productId}")
    public ResponseEntity<ApiResponse<ProductResponse>> getProductDetails (@PathVariable Long productId ,
                                                                           @RequestParam ViewType viewType,
                                                                           @RequestParam(required = false) Long cursor,
                                                                           @RequestParam(defaultValue = "10") int pageSize) {
        try {
            return ResponseEntity.ok(ApiResponse.onSuccess(productsQueryService.getProduct(productId, viewType, cursor, pageSize)));
        }
        catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ApiResponse.onFailure(HttpStatus.BAD_REQUEST.name(), e.getMessage()));
        } catch (ProductsExceptionHandler e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(ApiResponse.onFailure(ErrorCode.PRODUCT_NOT_FOUND.getCode(), ErrorCode.PRODUCT_NOT_FOUND.getMessage()));
        }
    }

    @Tag(name = "FavoriteProduct")
    @Operation(summary = "찜하지 않은 상태라면 찜하기. \n 찜한 상태라면 찜하기 취소")
    @PostMapping("/favorite/{productId}")
    public ResponseEntity<ApiResponse<Void>> toggleFavoriteProduct(@AuthenticationPrincipal Buyer currentBuyer, @PathVariable Long productId) {
        try {
            return ResponseEntity.ok(productsCommandService.toggleFavoriteProduct(productId, currentBuyer));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ApiResponse.onFailure(HttpStatus.BAD_REQUEST.name(), e.getMessage()));
        } catch (UserExceptionHandler e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(ApiResponse.onFailure(ErrorCode.BUYER_NOT_FOUND.getCode(), ErrorCode.BUYER_NOT_FOUND.getMessage()));
        }
    }

    @Tag(name = "Products")
    @Operation(summary = "판매 상품 목록 가져오기 + 필터링 ")
    @GetMapping("/list")
    public ResponseEntity<ApiResponse<ProductsListResponse>> findProductsByFilters(
            @RequestParam(required = false) String category,
            @RequestParam(required = false) String keyword,
            @RequestParam(required = false) Long minPrice,
            @RequestParam(required = false) Long maxPrice,
            @RequestParam(required = false) String sort,
            @RequestParam(required = false) Long cursor,
            @RequestParam(defaultValue = "10") int pageSize) {

        try {
            return ResponseEntity.ok(ApiResponse.onSuccess(productsQueryService.findProductsByFilters(category, keyword, minPrice, maxPrice, sort, cursor, pageSize)));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ApiResponse.onFailure(HttpStatus.BAD_REQUEST.name(), e.getMessage()));
        } catch (ProductsExceptionHandler e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(ApiResponse.onFailure(ErrorCode.PRODUCT_NOT_FOUND.getCode(), ErrorCode.PRODUCT_NOT_FOUND.getMessage()));
        }
    }

    // 상품 등록
    @Tag(name = "Products")
    @Operation(summary = "상품 등록, sellerId 제거 예정", description = "productRegister로 저장")
    @PostMapping(value = "/create", consumes = {MediaType.MULTIPART_FORM_DATA_VALUE})
    public ResponseEntity<ApiResponse<ProductRegisterResponse>> createProduct(@RequestPart("createProductDto") CreateProductDto request,
                                                                              @RequestPart(name = "image", required = false) List<MultipartFile> images,
                                                                              @RequestPart(name = "detailImage", required = false) List<MultipartFile> detailImages) {
        ApiResponse<ProductRegisterResponse> createdProduct = productsCommandService.createProduct(request, images, detailImages);
        return new ResponseEntity<>(createdProduct, HttpStatus.CREATED);
    }

    // 상품 수정
    @Tag(name = "Products")
    @Operation(summary = "상품 수정, sellerId 제거 예정")
    @PutMapping(value = "/{productId}", consumes = {MediaType.MULTIPART_FORM_DATA_VALUE})
    public ResponseEntity<ApiResponse<ProductUpdateResponse>> updateProduct(@PathVariable Long productId,
                                                                            @RequestPart("updateProductDto") UpdateProductDto request,
                                                                            @RequestPart(name = "image", required = false) List<MultipartFile> images,
                                                                            @RequestPart(name = "detailImage", required = false) List<MultipartFile> detailImages) {
        ApiResponse<ProductUpdateResponse> updatedProduct = productsCommandService.updateProduct(productId, request, images, detailImages);
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
