package umc.unimade.domain.accounts.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class SignInResponseDto {
    private Integer status;
    private String accessToken;
    private String refreshToken;

    public static SignInResponseDto from(Integer status, String accessToken, String refreshToken) {
        return SignInResponseDto.builder()
                .status(status)
                .accessToken(accessToken)
                .refreshToken(refreshToken)
                .build();
    }
}
