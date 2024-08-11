package umc.unimade.domain.products.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import umc.unimade.domain.accounts.entity.Buyer;
import umc.unimade.domain.accounts.exception.UserExceptionHandler;
import umc.unimade.domain.products.dto.ProductResponse;
import umc.unimade.domain.products.dto.ProductsListResponse;
import umc.unimade.domain.products.entity.ViewType;
import umc.unimade.domain.products.exception.ProductsExceptionHandler;
import umc.unimade.domain.products.service.ProductsCommandService;
import umc.unimade.domain.products.service.ProductsQueryService;
import umc.unimade.global.common.ApiResponse;
import umc.unimade.global.common.BaseEntity;
import umc.unimade.global.common.ErrorCode;
import umc.unimade.global.security.LoginBuyer;

import java.util.List;

@RestController
@RequestMapping("/buyer/product")
@RequiredArgsConstructor
public class BuyerProductController extends BaseEntity {
    private final ProductsQueryService productsQueryService;
    private final ProductsCommandService productsCommandService;

    @Tag(name = "Products", description = "판매 상품 관련 API")
    @Operation(summary = "판매 상품 정보 가져오기")
    @GetMapping("/{productId}")
    public ResponseEntity<ApiResponse<ProductResponse>> getProductDetails(@PathVariable Long productId,
                                                                          @RequestParam ViewType viewType,
                                                                          @RequestParam(required = false) Long cursor,
                                                                          @RequestParam(defaultValue = "10") int pageSize) {
        try {
            return ResponseEntity.ok(ApiResponse.onSuccess(productsQueryService.getProduct(productId, viewType, cursor, pageSize)));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ApiResponse.onFailure(HttpStatus.BAD_REQUEST.name(), e.getMessage()));
        } catch (ProductsExceptionHandler e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(ApiResponse.onFailure(ErrorCode.PRODUCT_NOT_FOUND.getCode(), ErrorCode.PRODUCT_NOT_FOUND.getMessage()));
        }
    }

    @Tag(name = "FavoriteProduct")
    @Operation(summary = "찜하지 않은 상태라면 찜하기. \n 찜한 상태라면 찜하기 취소")
    @PostMapping("/favorite/{productId}")
    public ResponseEntity<ApiResponse<Void>> toggleFavoriteProduct(@LoginBuyer Buyer buyer, @PathVariable Long productId) {
        try {
            return ResponseEntity.ok(productsCommandService.toggleFavoriteProduct(productId, buyer));
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
            @RequestParam(required = false) List<Long> categoryIds,
            @RequestParam(required = false) String keyword,
            @RequestParam(required = false) Long minPrice,
            @RequestParam(required = false) Long maxPrice,
            @RequestParam(required = false) String sort,
            @RequestParam(required = false) Long cursor,
            @RequestParam(defaultValue = "10") int pageSize) {

        try {
            return ResponseEntity.ok(ApiResponse.onSuccess(productsQueryService.findProductsByFilters(categoryIds, keyword, minPrice, maxPrice, sort, cursor, pageSize)));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ApiResponse.onFailure(HttpStatus.BAD_REQUEST.name(), e.getMessage()));
        } catch (ProductsExceptionHandler e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(ApiResponse.onFailure(ErrorCode.PRODUCT_NOT_FOUND.getCode(), ErrorCode.PRODUCT_NOT_FOUND.getMessage()));
        }
    }
}
