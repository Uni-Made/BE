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
    private Long optionId;
    private String name;
    private List<String> values;

    public static OptionResponse from(OptionCategory category) {
        List<String> values = category.getValues().stream()
                .map(OptionValue::getValue)
                .collect(Collectors.toList());

        return OptionResponse.builder()
                .optionId(category.getId())
                .name(category.getName())
                .values(values)
                .build();
    }
}
