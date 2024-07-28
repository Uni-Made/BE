package umc.unimade.domain.orders.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import umc.unimade.domain.accounts.entity.Buyer;
import umc.unimade.domain.orders.entity.OrderItem;
import umc.unimade.domain.orders.entity.OrderOption;
import umc.unimade.domain.orders.entity.Orders;
import umc.unimade.domain.orders.entity.PurchaseForm;
import umc.unimade.domain.products.entity.OptionValue;
import umc.unimade.domain.products.entity.PickupOption;
import umc.unimade.domain.products.entity.Products;

import java.util.List;
import java.util.stream.Collectors;

@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class OrderRequest {
    @NotNull
    private PurchaseFormRequest purchaseForm;
    @NotEmpty
    private List<OrderOptionRequest> orderOptions;

    @Getter
    @AllArgsConstructor
    @NoArgsConstructor
    @Builder
    public static class PurchaseFormRequest {
        private String name;
        private String phoneNumber;
        private PickupOption pickupOption;
        private String address;
        private String detailAddress;
        private Boolean isAgree;

        public PurchaseForm toEntity() {
            return PurchaseForm.builder()
                    .name(name)
                    .phoneNumber(phoneNumber)
                    .pickupOption(pickupOption)
                    .address(address)
                    .detailAddress(detailAddress)
                    .isAgree(isAgree)
                    .build();
        }
    }

    @Getter
    @AllArgsConstructor
    @NoArgsConstructor
    @Builder
    public static class OrderOptionRequest {
        @NotEmpty(message = "옵션을 선택해주세요.")
        private List<Long> optionValueIds;
        @Min(value = 1, message = "수량은 최소 1 이상이어야 합니다.")
        private int count;

        public List<OrderOption> toOrderOptions(OrderItem orderItem, List<OptionValue> optionValues) {
            return optionValues.stream()
                    .map(optionValue -> OrderOption.builder()
                            .orderItem(orderItem)
                            .optionValue(optionValue)
                            .build())
                    .collect(Collectors.toList());
        }
    }

    public Orders toOrders(Products product, Buyer buyer, PurchaseForm purchaseForm) {
        return Orders.builder()
                .product(product)
                .buyer(buyer)
                .purchaseForm(purchaseForm)
                .build();
    }

    public List<OrderItem> toOrderItems(Orders order, Products product) {
        return this.orderOptions.stream()
                .map(option -> OrderItem.builder()
                        .order(order)
                        .product(product)
                        .count(option.getCount())
                        .build())
                .collect(Collectors.toList());
    }
}
