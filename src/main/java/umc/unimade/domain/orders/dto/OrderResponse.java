package umc.unimade.domain.orders.dto;
import jakarta.persistence.criteria.Order;
import lombok.*;
import umc.unimade.domain.orders.entity.Orders;
import umc.unimade.domain.products.entity.Products;

@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class OrderResponse {
    private String bankName;
    private String accountNumber;
    private String accountName;
    private String buyerName;
    private Long totalPrice;

    public static OrderResponse fromOrder(Products products, Orders order, Long totalPrice) {
        return OrderResponse.builder()
                .bankName(products.getBankName())
                .accountNumber(products.getAccountNumber())
                .accountName(products.getAccountName())
                .buyerName(order.getPurchaseForm().getName())
                .totalPrice(totalPrice)
                .build();
    }
}
