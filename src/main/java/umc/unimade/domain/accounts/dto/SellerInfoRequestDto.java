package umc.unimade.domain.accounts.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class SellerInfoRequestDto {
    private String name;
    private String password;
    private String newPassword;
}
