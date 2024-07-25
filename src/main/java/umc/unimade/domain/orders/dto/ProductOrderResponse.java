package umc.unimade.domain.orders.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import umc.unimade.domain.orders.entity.Orders;

import java.time.LocalDateTime;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ProductOrderResponse { // 특정 상품의 온 구매 요청 확인

    private Long productId;
    private Long orderId;
    private String productImage;
    private String productName;
    private LocalDateTime createdAt;
    private String orderStatus;
    private String receiveStatus;

    public static ProductOrderResponse from(Orders order) {
        return ProductOrderResponse.builder()
                .productId(order.getProduct().getId())
                .orderId(order.getId())
                .productImage(order.getProduct().getProductImages().isEmpty() ? null : order.getProduct().getProductImages().get(0).getImageUrl())
                .productName(order.getProduct().getName())
                .createdAt(order.getPurchaseForm().getCreatedAt())
                .orderStatus(order.getStatus().toString())
                .receiveStatus(order.getReceiveStatus().toString())
                .build();
    }
}