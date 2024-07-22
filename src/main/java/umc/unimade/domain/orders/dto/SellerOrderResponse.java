package umc.unimade.domain.orders.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import umc.unimade.domain.orders.entity.Orders;

import java.time.LocalDateTime;

/*
상품 사진
 상품명
 구매요청일
 입금확인(구매_주문상태): 대기중/입금완료/수령완료
 수력확인: 수령대기/수령완료 -> 입금완료인 경우만 수령완료로 변경 가능
 */

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SellerOrderResponse { // 특정 판매자에게 온 구매 요청 확인

    private Long sellerId;
    private Long orderId;
    private String productImage;
    private String productName;
    private LocalDateTime createdAt;
    private String orderStatus;
    private String receiveStatus;

    public static SellerOrderResponse from(Orders order) {
        return SellerOrderResponse.builder()
                .sellerId(order.getProduct().getSeller().getId())
                .orderId(order.getId())
                .productImage(order.getProduct().getProductImages().get(0).getImageUrl()) // assuming the first image is the main one
                .productName(order.getProduct().getName())
                .createdAt(order.getCreatedAt())
                .orderStatus(order.getStatus().toString())
                .receiveStatus(order.getReceiveStatus().toString())
                .build();
    }
}