package umc.unimade.domain.products.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import lombok.*;
import org.springframework.web.multipart.MultipartFile;
import umc.unimade.domain.products.dto.ProductRequest.UpdateProductDto;
import umc.unimade.domain.products.dto.ProductResponse;
import umc.unimade.domain.products.dto.ProductRequest.CreateProductDto;
import umc.unimade.domain.products.entity.ProductRegister;
import umc.unimade.domain.products.entity.Products;
import umc.unimade.domain.products.entity.ViewType;
import umc.unimade.domain.products.service.ProductsCommandService;
import umc.unimade.domain.products.service.ProductsQueryService;
import umc.unimade.global.common.ApiResponse;
import umc.unimade.global.common.BaseEntity;
import umc.unimade.global.common.ErrorCode;
import umc.unimade.global.common.exception.ProductsExceptionHandler;
import umc.unimade.global.common.exception.UserExceptionHandler;

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
    //To do : 토큰 구현 시 user 정보 추가
    public ResponseEntity<ApiResponse<ProductResponse>> getProductDetails
            (@PathVariable Long productId , @RequestParam ViewType viewType, @RequestParam(required = false) Integer page, @RequestParam(required = false) Integer size) {
        try {
            PageRequest pageRequest = PageRequest.of(page, size);
            return ResponseEntity.ok(ApiResponse.onSuccess(productsQueryService.getProduct(productId, viewType, pageRequest)));
        }
        catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ApiResponse.onFailure(HttpStatus.BAD_REQUEST.name(), e.getMessage()));
        } catch (ProductsExceptionHandler e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(ApiResponse.onFailure(ErrorCode.PRODUCT_NOT_FOUND.getCode(), ErrorCode.PRODUCT_NOT_FOUND.getMessage()));
        }
    }

    //To do : buyerId 추후에 토큰으로 변경
    @Tag(name = "favoriteProduct", description = "상품 찜하기/취소 API")
    @Operation(summary = "찜하지 않은 상태라면 찜하기. \n 찜한 상태라면 찜하기 취소")
    @PostMapping("/favorite/{productId}/{buyerId}")
    public ResponseEntity<ApiResponse<Void>> toggleFavoriteProduct(@PathVariable Long productId, @PathVariable Long buyerId) {
        try {
            return ResponseEntity.ok(productsCommandService.toggleFavoriteProduct(productId, buyerId));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ApiResponse.onFailure(HttpStatus.BAD_REQUEST.name(), e.getMessage()));
        } catch (UserExceptionHandler e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(ApiResponse.onFailure(ErrorCode.BUYER_NOT_FOUND.getCode(), ErrorCode.BUYER_NOT_FOUND.getMessage()));
        }
    }

    // 상품 등록
    @Tag(name = "Products")
    @Operation(summary = "상품 등록", description = "productRegister로 저장")
    @PostMapping(value = "/create", consumes = {MediaType.MULTIPART_FORM_DATA_VALUE})
    public ResponseEntity<ApiResponse<ProductRegister>> createProduct(@RequestPart("createProductDto") CreateProductDto request,
                                                                      @RequestPart(name = "image", required = false) List<MultipartFile> images) {
        ApiResponse<ProductRegister> createdProduct = productsCommandService.createProduct(request, images);
        return new ResponseEntity<>(createdProduct, HttpStatus.CREATED);
    }

    // 상품 수정
    @Tag(name = "Products")
    @Operation(summary = "상품 수정", description = "product 수정")
    @PutMapping("/{productId}")
    public ResponseEntity<ApiResponse<Products>> updateProduct(@PathVariable Long productId,
                                                               @RequestBody UpdateProductDto request) {
        ApiResponse<Products> updatedProduct = productsCommandService.updateProduct(productId, request);
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
}