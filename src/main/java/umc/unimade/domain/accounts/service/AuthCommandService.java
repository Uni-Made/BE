package umc.unimade.domain.accounts.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import umc.unimade.domain.accounts.dto.*;
import umc.unimade.domain.accounts.entity.*;
import umc.unimade.domain.accounts.repository.BuyerRepository;
import umc.unimade.domain.accounts.repository.SellerRegisterRepository;
import umc.unimade.domain.accounts.repository.SellerRepository;
import umc.unimade.global.common.ErrorCode;
import umc.unimade.global.common.exception.CustomException;
import umc.unimade.global.security.JwtProvider;
import umc.unimade.global.security.JwtToken;
import umc.unimade.global.util.auth.OauthUtil;
import umc.unimade.global.util.auth.dto.OauthSignUpDto;
import umc.unimade.global.util.redis.RedisUtil;
import java.util.Map;

@Service
@RequiredArgsConstructor
@Transactional
public class AuthCommandService {
    private final BuyerRepository buyerRepository;
    private final OauthUtil oauthUtil;
    private final JwtProvider jwtProvider;
    private final RedisUtil redisUtil;
    private final SellerRepository sellerRepository;
    private final SellerRegisterRepository sellerRegisterRepository;

    public Object socialLogin(String authCode, Provider provider, String fcmToken) {
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
            return SignUpResponseDto.from(201, socialName, email, socialId);
        }
        redisUtil.saveFCMToken(email,fcmToken);
        JwtToken jwtToken = jwtProvider.createTotalToken(loginCustomer.getSocialId(), loginCustomer.getRole());
        loginCustomer.login(jwtToken.getRefreshToken());
        return SignInResponseDto.from(202, jwtToken.getAccessToken(), jwtToken.getRefreshToken());
    }

    public SignInResponseDto buyerSignUp(BuyerSignUpReqeustDto signUpReqeustDto, Role role) {
        Buyer loginBuyer = buyerRepository.findBySocialIdAndProvider(signUpReqeustDto.getSocialId(), signUpReqeustDto.getProvider());
        if (loginBuyer == null) {
            JwtToken jwtToken = jwtProvider.createTotalToken(signUpReqeustDto.getSocialId(), role);

            Buyer newBuyer = signUpReqeustDto.toEntity(signUpReqeustDto, jwtToken.getRefreshToken(), signUpReqeustDto.getSocialId());
            buyerRepository.save(newBuyer);
            newBuyer.login(jwtToken.getRefreshToken());
            return SignInResponseDto.from(202, jwtToken.getAccessToken(), jwtToken.getRefreshToken());
            //저장 로직
        } else {
            throw new CustomException(ErrorCode.USER_ALREADY_EXIST);
        }
    }

    public Boolean sellerSignUp(SellerSignUpRequestDto signUpReqeustDto, Role role) {
        Seller loginSeller = sellerRepository.findByEmailAndProvider(signUpReqeustDto.getEmail(), signUpReqeustDto.getProvider());
        if(loginSeller == null) {
            SellerRegister newSeller = signUpReqeustDto.toEntity(signUpReqeustDto);
            sellerRegisterRepository.save(newSeller);
            return true;
        } else {
            throw new CustomException(ErrorCode.USER_ALREADY_EXIST);
        }
    }

    public SignInResponseDto sellerSignIn(SignInRequestDto signInRequestDto, Role role) {
        Seller seller = sellerRepository.findByEmailAndPasswordAndProvider(signInRequestDto.getEmail(), signInRequestDto.getPassword(), Provider.NORMAL)
                .orElseThrow(() -> new CustomException(ErrorCode.USER_NOT_FOUND));
        redisUtil.saveFCMToken(signInRequestDto.getEmail(), signInRequestDto.getFcmToken());
        JwtToken jwtToken = jwtProvider.createTotalToken(seller.getEmail(), role);

        seller.login(jwtToken.getRefreshToken());
        return SignInResponseDto.from(202, jwtToken.getAccessToken(), jwtToken.getRefreshToken());
    }

    public boolean logout() {
        Map<String, String> info = oauthUtil.userInfo();
        switch (info.get("role")) {
            case "BUYER":
                Buyer buyerIsLogin = buyerRepository.findBySocialIdAndRefreshTokenIsNotNullAndIsLoginIsTrue(info.get("userId"));
                if (buyerIsLogin == null) {
                    throw new CustomException(ErrorCode.USER_NOT_FOUND);
                }

                //to do : jwtProvider에서 빼오는 것으로 수정
                //redisUtil.removeFCMToken();
                buyerIsLogin.logout();
                break;
            case "SELLER", "ADMIN":
                Seller sellerISLogin = sellerRepository.findByEmailAndRefreshTokenIsNotNullAndIsLoginIsTrue(info.get("userId"));

                if (sellerISLogin == null) {
                    throw new CustomException(ErrorCode.USER_NOT_FOUND);
                }
                sellerISLogin.logout();
                break;
        }
        return true;
    }
}
