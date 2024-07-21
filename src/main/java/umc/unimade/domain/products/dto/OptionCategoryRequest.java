package umc.unimade.domain.products.dto;

import lombok.*;

import java.util.List;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class OptionCategoryRequest {
    private String name;
    private List<String> values;
}
