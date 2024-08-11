package umc.unimade.domain.orders.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import umc.unimade.domain.orders.dto.SellerOrderResponse;
import umc.unimade.domain.orders.entity.OrderStatus;
import umc.unimade.domain.orders.entity.Orders;
import umc.unimade.domain.orders.service.OrderCommandService;
import umc.unimade.domain.orders.service.OrderQueryService;

@RestController
@RequestMapping("/seller/orders")
@RequiredArgsConstructor
public class SellerOrderController {
    private final OrderCommandService orderCommandService;
    private final OrderQueryService orderQueryService;

    @Tag(name = "Seller", description = "판매자 관련 API")
    @Operation(summary = "사용 X - 특정 판매자에게 온 구매 요청 보기")
    @GetMapping("/seller/{sellerId}")
    public ResponseEntity<Page<SellerOrderResponse>> getOrdersBySellerId(@PathVariable Long sellerId,
                                                                         @RequestParam(name = "page", defaultValue = "0") int page,
                                                                         @RequestParam(name = "size", defaultValue = "10") int size) {
        Pageable pageable = PageRequest.of(page, size);
        Page<SellerOrderResponse> orders = orderQueryService.getOrdersBySellerId(sellerId, pageable);
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
