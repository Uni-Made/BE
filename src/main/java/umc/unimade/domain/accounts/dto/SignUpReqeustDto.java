package umc.unimade.domain.accounts.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import umc.unimade.domain.accounts.entity.*;

//{
//	"name": "이름",
//	"email": "이메일",
//	"gender": "MALE|FEMALE",
//	"phone": "01099266634",
//	"provider": "KAKAO|GOOGLE|NAVER"
//}
@Getter
@AllArgsConstructor
public class SignUpReqeustDto {
    private String socialId;
    private String name;
    private String email;
    private Gender gender;
    private String phone;
    private Provider provider;

    public Buyer toEntityBuyer(SignUpReqeustDto signUpReqeustDto, String refreshToken, String socialId) {
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

    public Seller toEntitySeller(SignUpReqeustDto signUpReqeustDto, String password) {
        return Seller.builder()
                .name(signUpReqeustDto.getName())
                .email(signUpReqeustDto.getEmail())
                .password(password)
                .phone(signUpReqeustDto.getPhone())
                .provider(signUpReqeustDto.getProvider())
                .profileImage(null)
                .gender(signUpReqeustDto.getGender())
                .role(Role.SELLER)
                .isLogin(false)
                .build();
    }

}
