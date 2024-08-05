package umc.unimade.domain.accounts.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import umc.unimade.domain.accounts.entity.*;

@Getter
@AllArgsConstructor
public class BuyerSignUpReqeustDto {
    private String socialId;
    private String name;
    private String email;
    private Gender gender;
    private String phone;
    private Provider provider;

    public Buyer toEntity(BuyerSignUpReqeustDto signUpReqeustDto, String refreshToken, String socialId) {
        return Buyer.builder()
                .name(signUpReqeustDto.getName())
                .email(signUpReqeustDto.getEmail())
                .refreshToken(refreshToken)
                .gender(signUpReqeustDto.getGender())
                .phone(signUpReqeustDto.getPhone())
                .profileImage(null)
                .provider(signUpReqeustDto.getProvider())
                .socialId(socialId)
                .role(Role.BUYER)
                .isLogin(false)
                .build();
    }

}
