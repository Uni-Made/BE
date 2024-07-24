package umc.unimade.global.security;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
@AllArgsConstructor
public class JwtToken {
    //private String grantType; //JWT에 대한 인증 타입
    private String accessToken;
    private String refreshToken;
}
