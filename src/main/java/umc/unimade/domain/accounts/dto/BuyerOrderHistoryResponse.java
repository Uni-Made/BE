package umc.unimade.domain.accounts.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import umc.unimade.domain.orders.entity.Orders;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class BuyerOrderHistoryResponse {
    private List<OrderHistoryItem> orders;
    private Long nextCursor;
    private Boolean isLast;


    @Getter
    @AllArgsConstructor
    @NoArgsConstructor
    @Builder
    public static class OrderHistoryItem{
        private Long orderId;
        private Long productId;
        private String productName;
        private String productImage;
        private String orderStatus;
        private LocalDateTime orderTime;

        public static OrderHistoryItem from(Orders order) {
            return OrderHistoryItem.builder()
                    .orderId(order.getId())
                    .productId(order.getProduct().getId())
                    .productName(order.getProduct().getName())
                    .productImage(order.getProduct().getProductImages().isEmpty() ? null : order.getProduct().getProductImages().get(0).getImageUrl())
                    .orderStatus(order.getStatus().name())
                    .orderTime(order.getCreatedAt())
                    .build();
        }
    }
    public static BuyerOrderHistoryResponse from(List<Orders> orders, Long nextCursor, Boolean isLast) {
        List<OrderHistoryItem>  orderItems = orders.stream()
                .map(OrderHistoryItem::from)
                .collect(Collectors.toList());
        return BuyerOrderHistoryResponse.builder()
                .orders(orderItems)
                .nextCursor(nextCursor)
                .isLast(isLast)
                .build();
    }
}
