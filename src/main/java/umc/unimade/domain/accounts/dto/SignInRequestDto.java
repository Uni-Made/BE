package umc.unimade.domain.accounts.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
@NoArgsConstructor(force = true)
public class SignInRequestDto {
    private final String email;
    private final String password;
    private final String fcmToken;
}
