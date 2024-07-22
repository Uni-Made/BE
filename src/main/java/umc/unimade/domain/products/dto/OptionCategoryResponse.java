package umc.unimade.domain.products.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import umc.unimade.domain.products.entity.OptionCategory;

import java.util.List;
import java.util.stream.Collectors;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class OptionCategoryResponse {

    private Long id;
    private String name;
    private List<String> values;

    public static OptionCategoryResponse from(OptionCategory optionCategory) {
        return OptionCategoryResponse.builder()
                .id(optionCategory.getId())
                .name(optionCategory.getName())
                .values(optionCategory.getValues().stream()
                        .map(value -> value.getValue())
                        .collect(Collectors.toList()))
                .build();
    }
}
