package umc.unimade.domain.products;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import lombok.*;
import umc.unimade.domain.products.dto.ProductResponse;
import umc.unimade.domain.products.entity.ViewType;
import umc.unimade.domain.products.service.ProductsCommandService;
import umc.unimade.domain.products.service.ProductsQueryService;
import umc.unimade.global.common.ApiResponse;
import umc.unimade.global.common.ErrorCode;
import umc.unimade.global.common.exception.ProductsExceptionHandler;
import umc.unimade.global.common.exception.UserExceptionHandler;


@RestController
@RequestMapping("/api/products")
@RequiredArgsConstructor
public class ProductsController {
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
    @Tag(name = "favoriteProduct", description = "상품 찜하기 API")
    @Operation(summary = "구매자가 상품을 찜하기")
    @PostMapping("/favorite/{productId}/{buyerId}")
    public ResponseEntity<ApiResponse<Void>> addFavoriteProduct(@PathVariable Long productId, @PathVariable Long buyerId) {
        try {
            productsCommandService.addFavoriteProduct(productId, buyerId);
            return ResponseEntity.ok(ApiResponse.onSuccess(null));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ApiResponse.onFailure(HttpStatus.BAD_REQUEST.name(), e.getMessage()));
        } catch (UserExceptionHandler e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(ApiResponse.onFailure(ErrorCode.BUYER_NOT_FOUND.getCode(), ErrorCode.BUYER_NOT_FOUND.getMessage()));
        }
    }


}
