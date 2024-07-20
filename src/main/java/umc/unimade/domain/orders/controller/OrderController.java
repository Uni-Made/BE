package umc.unimade.domain.orders.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.*;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import umc.unimade.domain.orders.dto.OrderRequest;
import umc.unimade.domain.orders.dto.OrderResponse;
import umc.unimade.domain.orders.dto.OrderVerificationRequest;
import umc.unimade.domain.orders.dto.OrderVerificationResponse;
import umc.unimade.domain.orders.service.OrderCommandService;
import umc.unimade.domain.orders.service.OrderQueryService;
import umc.unimade.global.common.ApiResponse;
import umc.unimade.global.common.ErrorCode;
import umc.unimade.domain.products.exception.ProductsExceptionHandler;

@RestController
@RequestMapping("/api/orders")
@RequiredArgsConstructor
@Tag(name = "Order", description = "구매 관련 API")
public class OrderController {
    private final OrderCommandService orderCommandService;
    private final OrderQueryService orderQueryService;

    @Operation(summary = "상품 구매하기")
    @PostMapping("/{productId}/{buyerId}")
    public ResponseEntity<ApiResponse<OrderResponse>> createOrder (@PathVariable Long productId, @PathVariable Long buyerId, @RequestBody OrderRequest orderRequest){
        try {
            return ResponseEntity.ok(ApiResponse.onSuccess(orderCommandService.createOrder(productId, buyerId, orderRequest)));
        }
        catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ApiResponse.onFailure(HttpStatus.BAD_REQUEST.name(), e.getMessage()));
        } catch (ProductsExceptionHandler e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(ApiResponse.onFailure(ErrorCode.PRODUCT_NOT_FOUND.getCode(), ErrorCode.PRODUCT_NOT_FOUND.getMessage()));
        }
    }

    @Operation(summary = "선택한 구매 옵션 확인하기")
    @PostMapping("/verify/{productId}")
    public ResponseEntity<ApiResponse<OrderVerificationResponse>>verifyOrder(@PathVariable Long productId, @RequestBody OrderVerificationRequest request){
        try{
            return ResponseEntity.ok(ApiResponse.onSuccess(orderQueryService.verifyOrder(productId,request)));
        }catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ApiResponse.onFailure(HttpStatus.BAD_REQUEST.name(), e.getMessage()));
        } catch (ProductsExceptionHandler e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(ApiResponse.onFailure(ErrorCode.PRODUCT_NOT_FOUND.getCode(), ErrorCode.PRODUCT_NOT_FOUND.getMessage()));
        }
    }
}
