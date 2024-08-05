package umc.unimade.domain.accounts.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import umc.unimade.domain.accounts.entity.*;
import umc.unimade.global.registerStatus.RegisterStatus;

@Getter
@AllArgsConstructor
public class SellerSignUpRequestDto {
    private String name;
    private String email;
    private String password;
    private Gender gender;
    private String phone;
    private Provider provider;

    public SellerRegister toEntity(SellerSignUpRequestDto signUpReqeustDto) {
        return SellerRegister.builder()
                .name(signUpReqeustDto.getName())
                .email(signUpReqeustDto.getEmail())
                .password(signUpReqeustDto.getPassword())
                .phone(signUpReqeustDto.getPhone())
                .provider(signUpReqeustDto.getProvider())
                .profileImage(null)
                .gender(signUpReqeustDto.getGender())
                .role(Role.SELLER)
                .registerStatus(RegisterStatus.PENDING)
                .build();
    }
}
