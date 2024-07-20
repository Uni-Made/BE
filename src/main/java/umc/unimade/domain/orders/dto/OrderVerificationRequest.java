package umc.unimade.domain.orders.dto;

import lombok.*;
import umc.unimade.domain.products.entity.OptionValue;

import java.util.List;
import java.util.stream.Collectors;

@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class OrderVerificationRequest {
    private List<OrderOptionRequest> orderOptions;

    @Getter
    @AllArgsConstructor
    @NoArgsConstructor
    @Builder
    public static class OrderOptionRequest {
        private List<Long> optionValueIds;
        private int count;

        public static OrderOptionRequest fromEntity(List<OptionValue> optionValues, int count) {
            List<Long> optionValueIds = optionValues.stream().map(OptionValue::getId).collect(Collectors.toList());
            return OrderOptionRequest.builder()
                    .optionValueIds(optionValueIds)
                    .count(count)
                    .build();
        }
    }
}