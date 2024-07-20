package umc.unimade.domain.orders.dto;

import lombok.*;
import umc.unimade.domain.products.entity.OptionValue;

import java.util.List;
import java.util.stream.Collectors;

@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class OrderVerificationResponse {
    private List<OrderOptionResponse> orderOptions;
    private Long totalPrice;

    @Getter
    @AllArgsConstructor
    @NoArgsConstructor
    @Builder
    public static class OrderOptionResponse {
        private List<String> optionValues;
        private int count;

        public static OrderOptionResponse of(List<OptionValue> optionValues, int count) {
            List<String> optionValueStrings = optionValues.stream().map(OptionValue::getValue).collect(Collectors.toList());
            return OrderOptionResponse.builder()
                    .optionValues(optionValueStrings)
                    .count(count)
                    .build();
        }
    }

    public static OrderVerificationResponse from(List<OrderOptionResponse> orderOptionResponses, Long totalPrice) {
        return OrderVerificationResponse.builder()
                .orderOptions(orderOptionResponses)
                .totalPrice(totalPrice)
                .build();
    }
}