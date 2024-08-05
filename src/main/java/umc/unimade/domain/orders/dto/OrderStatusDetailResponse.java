package umc.unimade.domain.orders.dto;

import lombok.*;
import umc.unimade.domain.orders.entity.OrderItem;
import umc.unimade.domain.orders.entity.Orders;
import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class OrderStatusDetailResponse {
    private String productName;
    private List<OrderOptionResponse> orderOptions;
    private Long totalPrice;
    private String buyerName;
    private String bankName;
    private String accountNumber;
    private String accountName;
    private LocalDate pickupDate;
    private String pickupAddress;
    private LocalDate deliverDate;

    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    public static class OrderOptionResponse {
        private List<String> optionValues;
        private int count;

        public static OrderOptionResponse fromOrderItem(OrderItem orderItem) {
            List<String> optionValueStrings = orderItem.getOrderOptions().stream()
                    .map(orderOption -> orderOption.getOptionValue().getValue())
                    .collect(Collectors.toList());
            return OrderOptionResponse.builder()
                    .optionValues(optionValueStrings)
                    .count(orderItem.getCount())
                    .build();
        }
    }

    public static OrderStatusDetailResponse fromPendingOrder(Orders order) {
        return commonResponse(order)
                .bankName(order.getProduct().getBankName())
                .accountNumber(order.getProduct().getAccountNumber())
                .accountName(order.getProduct().getAccountName())
                .build();
    }

    public static OrderStatusDetailResponse fromPaidOfflineOrder(Orders order) {
        return commonResponse(order)
                .pickupDate(order.getPurchaseForm().getPickupDate())
                .pickupAddress(order.getProduct().getPickupLocation())
                .build();
    }

    public static OrderStatusDetailResponse fromPaidOnlineOrder(Orders order) {
        return commonResponse(order)
                .deliverDate(order.getProduct().getPickupDate())
                .build();
    }

    private static List<OrderOptionResponse> getOrderOptionResponses(Orders order) {
        return order.getOrderItems().stream()
                .map(OrderOptionResponse::fromOrderItem)
                .collect(Collectors.toList());
    }

    private static OrderStatusDetailResponse.OrderStatusDetailResponseBuilder commonResponse(Orders order) {
        return OrderStatusDetailResponse.builder()
                .productName(order.getProduct().getName())
                .orderOptions(getOrderOptionResponses(order))
                .totalPrice(order.getTotalPrice())
                .buyerName(order.getBuyer().getName());
    }
}
