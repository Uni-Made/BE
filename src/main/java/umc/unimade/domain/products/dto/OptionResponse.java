package umc.unimade.domain.products.dto;

import lombok.*;
import umc.unimade.domain.products.entity.OptionCategory;
import umc.unimade.domain.products.entity.OptionValue;

import java.util.List;
import java.util.stream.Collectors;

@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class OptionResponse {
    private Long optionCategoryId;
    private String optionCategory;
    private List<OptionValueResponse> optionValues;

    @Getter
    @AllArgsConstructor
    @NoArgsConstructor
    @Builder
    public static class OptionValueResponse {
        private Long valueId;
        private String value;

        public static OptionValueResponse from(OptionValue optionValue) {
            return OptionValueResponse.builder()
                    .valueId(optionValue.getId())
                    .value(optionValue.getValue())
                    .build();
        }
    }

    public static OptionResponse from(OptionCategory category) {
        return OptionResponse.builder()
                .optionCategoryId(category.getId())
                .optionCategory(category.getName())
                .optionValues(category.getValues().stream()
                        .map(OptionValueResponse::from)
                        .collect(Collectors.toList()))
                .build();
    }
}
