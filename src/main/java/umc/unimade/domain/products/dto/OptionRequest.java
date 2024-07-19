package umc.unimade.domain.products.dto;

import lombok.*;
import umc.unimade.domain.products.entity.Options;
import umc.unimade.domain.products.entity.ProductRegister;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class OptionRequest {
    private String name;
    private String value;

    public Options toEntity(ProductRegister productRegister) {
        return Options.builder()
                .name(name)
                .value(value)
                .productRegister(productRegister)
                .build();
    }
}
