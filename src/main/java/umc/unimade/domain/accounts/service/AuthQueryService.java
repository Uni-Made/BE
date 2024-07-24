package umc.unimade.domain.accounts.service;

import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import umc.unimade.domain.accounts.dto.*;
import umc.unimade.domain.accounts.entity.Buyer;
import umc.unimade.domain.accounts.entity.Provider;
import umc.unimade.domain.accounts.entity.Role;
import umc.unimade.domain.accounts.entity.Seller;
import umc.unimade.domain.accounts.repository.BuyerRepository;
import umc.unimade.domain.accounts.repository.SellerRepository;
import umc.unimade.domain.accounts.repository.SmsCertification;
import umc.unimade.global.common.exception.CustomException;
import umc.unimade.global.common.exception.GlobalErrorCode;
import umc.unimade.global.security.JwtProvider;
import umc.unimade.global.security.JwtToken;
import umc.unimade.global.security.UserLoginForm;
import umc.unimade.global.util.auth.OauthUtil;
import umc.unimade.global.util.auth.SmsUtil;
import umc.unimade.global.util.auth.dto.OauthSignUpDto;

import java.util.HashMap;
import java.util.Map;

@Service
@RequiredArgsConstructor
@Transactional
public class AuthQueryService {
    private final BuyerRepository buyerRepository;
    private final OauthUtil oauthUtil;
    private final JwtProvider jwtProvider;
    private final SmsUtil smsUtil;
    private final SmsCertification smsCertification;
    private final SellerRepository sellerRepository;

    public Object socialLogin(String authCode, Provider provider) {
        String socialId = null;
        String socialName = null;
        String email = null;
        switch (provider) {
            case KAKAO:
                String kakaoAccessToken = oauthUtil.getKakaoAccessToken(authCode);
                OauthSignUpDto kakaoInfo = oauthUtil.getKakaoUserInfo(kakaoAccessToken);
                email = kakaoInfo.getSocialEmail();
                socialId = kakaoInfo.getSocialId();
                socialName = kakaoInfo.getSocialName();
                break;
            case NAVER:
                String naverAccessToken = oauthUtil.getNaverAccessToken(authCode);
                OauthSignUpDto naverInfo = oauthUtil.getNaverUserInfo(naverAccessToken);
                email = naverInfo.getSocialEmail();
                socialId = naverInfo.getSocialId();
                socialName = naverInfo.getSocialName();
                break;
            case GOOGLE:
                String googleAccessToken = oauthUtil.getGoogleAccessToken(authCode);
                OauthSignUpDto googleInfo = oauthUtil.getGoogleUserInfo(googleAccessToken);
                email = googleInfo.getSocialEmail();
                socialId = googleInfo.getSocialId();
                socialName = googleInfo.getSocialName();
                break;
        }

        Buyer loginCustomer = buyerRepository.findBySocialIdAndProvider(socialId, provider);
        if (loginCustomer == null) {
            return SignUpResponseDto.builder()
                    .status(201)
                    .email(email)
                    .socialName(socialName)
                    .socialId(socialId)
                    .build();
        }

        JwtToken jwtToken = jwtProvider.createTotalToken(loginCustomer.getSocialId(), loginCustomer.getRole());
        loginCustomer.login(jwtToken.getRefreshToken());

        return SignInResponseDto.builder()
                .status(202)
                .accessToken(jwtToken.getAccessToken())
                .refreshToken(jwtToken.getRefreshToken())
                .build();
    }

    public ReissueTokenResponseDto reissueToken(HttpServletRequest request, Role role) {
        return ReissueTokenResponseDto.builder()
                .accessToken(jwtProvider.reissueToken(request, role))
                .build();
    }

    public SignInResponseDto buyerSignUp(SignUpReqeustDto signUpReqeustDto, Role role) {
        Buyer loginBuyer = buyerRepository.findBySocialIdAndProvider(signUpReqeustDto.getSocialId(), signUpReqeustDto.getProvider());
        if (loginBuyer == null) {
            JwtToken jwtToken = jwtProvider.createTotalToken(signUpReqeustDto.getSocialId(), role);
            System.err.println(jwtToken.getRefreshToken());
            Buyer newBuyer = signUpReqeustDto.toEntityBuyer(signUpReqeustDto, jwtToken.getRefreshToken(), signUpReqeustDto.getSocialId());
            buyerRepository.save(newBuyer);
            newBuyer.login(jwtToken.getRefreshToken());
            return SignInResponseDto.builder()
                    .accessToken(jwtToken.getAccessToken())
                    .refreshToken(jwtToken.getRefreshToken())
                    .build();
            //저장 로직
        } else {
            throw new CustomException(GlobalErrorCode.USER_ALREADY_EXISTS);
        }
    }

    public SignInResponseDto sellerSignUp(SignUpReqeustDto signUpReqeustDto, Role role) {
        Seller loginSeller = sellerRepository.findByEmailAndProvider(signUpReqeustDto.getSocialId(), signUpReqeustDto.getProvider());
        if(loginSeller == null) {
            JwtToken jwtToken = jwtProvider.createTotalToken(signUpReqeustDto.getSocialId(), role);
            Seller newSeller = signUpReqeustDto.toEntitySeller(signUpReqeustDto, jwtToken.getRefreshToken());
            sellerRepository.save(newSeller);
            newSeller.login(jwtToken.getRefreshToken());
            return SignInResponseDto.builder()
                    .accessToken(jwtToken.getAccessToken())
                    .refreshToken(jwtToken.getRefreshToken())
                    .build();
        }
        return null;
    }

    public SignInResponseDto sellerSignIn(SignInRequestDto signInRequestDto, Role role) {
        Seller seller = sellerRepository.findByEmailAndPasswordAndProvider(signInRequestDto.getEmail(), signInRequestDto.getPassword(), Provider.NORMAL)
                .orElseThrow(() -> new CustomException(GlobalErrorCode.USER_NOT_FOUND));
        JwtToken jwtToken = jwtProvider.createTotalToken(seller.getEmail(), role);

        seller.login(jwtToken.getRefreshToken());
        return SignInResponseDto.builder()
                .accessToken(jwtToken.getAccessToken())
                .refreshToken(jwtToken.getRefreshToken())
                .build();
    }

    public boolean logout() {
        Map<String, String> info = oauthUtil.userInfo();
        switch (info.get("role")) {
            case "BUYER":
                Buyer buyerIsLogin = buyerRepository.findBySocialIdAndRefreshTokenIsNotNullAndIsLoginIsTrue(info.get("userId"));
                if (buyerIsLogin == null) {
                    throw new CustomException(GlobalErrorCode.USER_NOT_FOUND);
                }
                buyerIsLogin.logout();
                break;
            case "SELLER", "ADMIN":
                Seller sellerISLogin = sellerRepository.findByEmailAndRefreshTokenIsNotNullAndIsLoginIsTrue(info.get("userId"));

                if (sellerISLogin == null) {
                    throw new CustomException(GlobalErrorCode.USER_NOT_FOUND);
                }
                sellerISLogin.logout();
                break;
        }
        return true;
    }

    public String authSmsSend(String phone) {
        String code = smsUtil.sendSMS(phone);;
        if(code.isEmpty()){
           throw new CustomException(GlobalErrorCode.SMS_SEND_FAILED);
        }
        smsCertification.createSmsCertification(phone, code);

        return "전송 성공";
    }

    public Boolean authSmsVerify(SmsVerifyRequestDto smsVerifyRequestDto) {
        if (isVerify(smsVerifyRequestDto)) {
            throw new IllegalArgumentException("인증번호가 일치하지 않습니다.");
        }
        smsCertification.deleteSmsCertification(smsVerifyRequestDto.getPhoneNumber());

        return true;
    }

    private boolean isVerify(SmsVerifyRequestDto smsVerifyRequestDto) {
        return !(smsCertification.hasKey(smsVerifyRequestDto.getPhoneNumber()) &&
                smsCertification.getSmsCertification(smsVerifyRequestDto.getPhoneNumber())
                        .equals(smsVerifyRequestDto.getCertificationNumber()));
    }

}
