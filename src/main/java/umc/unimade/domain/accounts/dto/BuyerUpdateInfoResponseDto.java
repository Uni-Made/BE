package umc.unimade.domain.accounts.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@AllArgsConstructor
@Builder
public class BuyerUpdateInfoResponseDto {
    private String name;

    public static BuyerUpdateInfoResponseDto of(String name) {
        return BuyerUpdateInfoResponseDto.builder()
                .name(name)
                .build();
    }
}
