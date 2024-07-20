package umc.unimade.domain.orders.dto;

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
    private PurchaseFormRequest purchaseForm;
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
                    .name(this.name)
                    .phoneNumber(this.phoneNumber)
                    .pickupOption(this.pickupOption)
                    .address(this.address)
                    .detailAddress(this.detailAddress)
                    .isAgree(this.isAgree)
                    .build();
        }
    }

    @Getter
    @AllArgsConstructor
    @NoArgsConstructor
    @Builder
    public static class OrderOptionRequest {
        private List<Long> optionValueIds;
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
