package umc.unimade.domain.accounts.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class SmsVerifyRequestDto {
    private String phoneNumber;
    private String certificationNumber;
}
