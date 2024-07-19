package umc.unimade.domain.products;

import io.swagger.v3.oas.annotations.Operation;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import lombok.*;
import umc.unimade.domain.products.dto.ProductResponse;
import umc.unimade.domain.products.service.ProductsQueryService;
import umc.unimade.global.common.ApiResponse;

@RestController
@RequestMapping("/api/products")
@RequiredArgsConstructor
public class ProductsController {
    private final ProductsQueryService productsQueryService;

    @Operation(summary = "판매 상품 정보 가져오기")
    @GetMapping("/{productId}")
    public ResponseEntity<ApiResponse<ProductResponse>> getProductDetails
            (@PathVariable Long productId , @RequestParam String viewType, @RequestParam(required = false) Integer page, @RequestParam(required = false) Integer size) {

        try {
            PageRequest pageRequest = PageRequest.of(page, size);
            return ResponseEntity.ok(ApiResponse.onSuccess(productsQueryService.getProductDetails(productId, viewType, pageRequest)));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ApiResponse.onFailure(HttpStatus.BAD_REQUEST.name(), e.getMessage()));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(ApiResponse.onFailure(HttpStatus.INTERNAL_SERVER_ERROR.name(), "An unexpected error occurred"));
        }
    }
}
