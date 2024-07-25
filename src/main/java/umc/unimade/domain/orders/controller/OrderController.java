package umc.unimade.domain.orders.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.*;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import umc.unimade.domain.accounts.entity.Seller;
import umc.unimade.domain.orders.dto.*;
import umc.unimade.domain.accounts.exception.UserExceptionHandler;
import umc.unimade.domain.orders.dto.OrderRequest;
import umc.unimade.domain.orders.dto.OrderResponse;
import umc.unimade.domain.orders.dto.OrderVerificationRequest;
import umc.unimade.domain.orders.dto.OrderVerificationResponse;
import umc.unimade.domain.orders.service.OrderCommandService;
import umc.unimade.domain.orders.service.OrderQueryService;
import umc.unimade.domain.products.entity.ViewType;
import umc.unimade.domain.orders.entity.Orders;
import umc.unimade.domain.orders.entity.OrderStatus;
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

    @Tag(name = "Order", description = "구매 관련 API")
    @Operation(summary = "대기 중일때 입금 정보 안내")
    @GetMapping("/{orderId}/banking-info")
    public ResponseEntity<ApiResponse<OrderResponse>>getBankingInfo(@PathVariable Long orderId){
        try{
            return ResponseEntity.ok(ApiResponse.onSuccess(orderQueryService.getBankingInfo(orderId)));
        }catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ApiResponse.onFailure(HttpStatus.BAD_REQUEST.name(), e.getMessage()));
        } catch (UserExceptionHandler e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(ApiResponse.onFailure(ErrorCode.ORDER_NOT_FOUND.getCode(), ErrorCode.ORDER_NOT_FOUND.getMessage()));
        }
    }

    @Tag(name = "Seller", description = "판매자 관련 API")
    @Operation(summary = "특정 판매자에게 온 구매 요청 보기")
    @GetMapping("/seller/{sellerId}")
    public ResponseEntity<List<SellerOrderResponse>> getOrdersBySellerId(@PathVariable Long sellerId,
                                                                         @RequestParam(name = "page", defaultValue = "0") int page,
                                                                         @RequestParam(name = "size", defaultValue = "10") int size) {
        PageRequest pageRequest = PageRequest.of(page, size);
        List<SellerOrderResponse> orders = orderQueryService.getOrdersBySellerId(sellerId, pageRequest);
        return ResponseEntity.ok(orders);
    }

    @Tag(name = "Order", description = "판매자 관련 API")
    @Operation(summary = "특정 상품의 구매 요청 보기")
    @GetMapping("/product/{productId}")
    public ResponseEntity<List<ProductOrderResponse>> getOrdersByProductId(@PathVariable Long productId,
                                                                         @RequestParam(name = "page", defaultValue = "0") int page,
                                                                         @RequestParam(name = "size", defaultValue = "10") int size) {
        PageRequest pageRequest = PageRequest.of(page, size);
        List<ProductOrderResponse> orders = orderQueryService.getOrdersByProductId(productId, pageRequest);
        return ResponseEntity.ok(orders);
    }

    @Tag(name = "Seller", description = "판매자 관련 API")
    @Operation(summary = "주문 상태 변경하기(PENDING,PAID,RECEIVED)")
    @PutMapping("/orderStatus/{orderId}")
    public ResponseEntity<SellerOrderResponse> changeOrderStatus(@PathVariable Long orderId, @RequestParam OrderStatus status) {
        Orders order = orderCommandService.changeOrderStatus(orderId, status);
        SellerOrderResponse updatedOrder = SellerOrderResponse.from(order);
        return ResponseEntity.ok(updatedOrder);
    }

    @Tag(name = "Seller", description = "판매자 관련 API")
    @Operation(summary = "수령 상태 변경하기(NOT_RECEIVED, RECEIVED)")
    @PutMapping("/receiveStatus/{orderId}")
    public ResponseEntity<SellerOrderResponse> changeReceiveStatus(@PathVariable Long orderId) {
        Orders order = orderCommandService.changeReceiveStatus(orderId);
        SellerOrderResponse updatedOrder = SellerOrderResponse.from(order);
        return ResponseEntity.ok(updatedOrder);
    }
}
