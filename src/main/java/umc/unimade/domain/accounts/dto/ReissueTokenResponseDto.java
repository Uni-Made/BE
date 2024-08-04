package umc.unimade.domain.accounts.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ReissueTokenResponseDto {
    private String accessToken;

    public static ReissueTokenResponseDto from(String accessToken) {
        return ReissueTokenResponseDto.builder()
                .accessToken(accessToken)
                .build();
    }
}
