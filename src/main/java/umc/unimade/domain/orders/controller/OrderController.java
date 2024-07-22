package umc.unimade.domain.orders.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.*;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import umc.unimade.domain.orders.dto.*;
import umc.unimade.domain.orders.service.OrderCommandService;
import umc.unimade.domain.orders.service.OrderQueryService;
import umc.unimade.domain.products.entity.ViewType;
import umc.unimade.global.common.ApiResponse;
import umc.unimade.global.common.ErrorCode;
import umc.unimade.domain.products.exception.ProductsExceptionHandler;

import java.util.List;

@RestController
@RequestMapping("/api/orders")
@RequiredArgsConstructor
public class OrderController {
    private final OrderCommandService orderCommandService;
    private final OrderQueryService orderQueryService;

    @Tag(name = "Order", description = "구매 관련 API")
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

    @Tag(name = "Order", description = "구매 관련 API")
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

    @Tag(name = "Seller", description = "판매자 관련 API")
    @Operation(summary = "특정 판매자에게 온 구매 요청 보기")
    @GetMapping("/{sellerId}")
    public ResponseEntity<List<SellerOrderResponse>> getOrdersBySellerId(@PathVariable Long sellerId,
                                                                         @RequestParam(name = "page", defaultValue = "0") int page,
                                                                         @RequestParam(name = "size", defaultValue = "10") int size) {
        PageRequest pageRequest = PageRequest.of(page, size);
        List<SellerOrderResponse> orders = orderQueryService.getOrdersBySellerId(sellerId, pageRequest);
        return ResponseEntity.ok(orders);
    }
}