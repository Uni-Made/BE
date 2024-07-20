package umc.unimade.domain.accounts.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import umc.unimade.domain.accounts.entity.Gender;
import umc.unimade.domain.accounts.entity.Provider;
import umc.unimade.domain.accounts.entity.SellerRegister;
import umc.unimade.global.registerStatus.RegisterStatus;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SellerRegisterResponse {

    private Long id;
    private String name;
    private String email;
    private String password;
    private Gender gender;
    private String phone;
    private String profileImage;
    private Provider provider;
    private RegisterStatus registerStatus;
    private String reason;

    public static SellerRegisterResponse from(SellerRegister sellerRegister) {
        return SellerRegisterResponse.builder()
                .id(sellerRegister.getId())
                .name(sellerRegister.getName())
                .email(sellerRegister.getEmail())
                .password(sellerRegister.getPassword())
                .gender(sellerRegister.getGender())
                .phone(sellerRegister.getPhone())
                .profileImage(sellerRegister.getProfileImage())
                .provider(sellerRegister.getProvider())
                .registerStatus(sellerRegister.getRegisterStatus())
                .reason(sellerRegister.getReason())
                .build();
    }
}