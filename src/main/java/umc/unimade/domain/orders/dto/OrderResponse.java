package umc.unimade.domain.orders.dto;
import lombok.*;
import umc.unimade.domain.orders.entity.Orders;
import umc.unimade.domain.products.entity.Products;

@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class OrderResponse {
    private Long orderId;
    private String bankName;
    private String accountNumber;
    private String accountName;
    private String buyerName;
    private Long totalPrice;

    public static OrderResponse from(Orders order, Products product, Long totalPrice) {
        return OrderResponse.builder()
                .orderId(order.getId())
                .bankName(product.getBankName())
                .accountNumber(product.getAccountNumber())
                .accountName(product.getAccountName())
                .buyerName(order.getBuyer().getName())
                .totalPrice(totalPrice)
                .build();
    }

}
