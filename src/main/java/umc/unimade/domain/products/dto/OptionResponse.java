package umc.unimade.domain.products.dto;
import lombok.*;
import umc.unimade.domain.products.entity.OptionCategory;

@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class OptionResponse {
    private Long optionId;
    private String name;


    public static OptionResponse to(OptionCategory option){
        return OptionResponse.builder()
                .optionId(option.getId())
                .name(option.getName())
                .build();
    }
}
