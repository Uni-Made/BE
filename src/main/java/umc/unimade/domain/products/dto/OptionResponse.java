package umc.unimade.domain.products.dto;
import lombok.*;
import umc.unimade.domain.products.entity.Options;

@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class OptionResponse {
    private Long optionId;
    private String name;
    private String value;

    public static OptionResponse to(Options option){
        return OptionResponse.builder()
                .optionId(option.getId())
                .name(option.getName())
                .value(option.getValue())
                .build();
    }
}
