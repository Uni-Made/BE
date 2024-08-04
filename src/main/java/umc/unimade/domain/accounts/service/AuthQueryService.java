package umc.unimade.domain.accounts.service;

import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import umc.unimade.domain.accounts.dto.*;
import umc.unimade.domain.accounts.entity.*;
import umc.unimade.domain.accounts.repository.BuyerRepository;
import umc.unimade.domain.accounts.repository.SellerRegisterRepository;
import umc.unimade.domain.accounts.repository.SellerRepository;
import umc.unimade.domain.accounts.repository.SmsCertification;
import umc.unimade.domain.notification.repository.FcmTokenRepository;
import umc.unimade.global.common.ErrorCode;
import umc.unimade.global.common.exception.CustomException;
import umc.unimade.global.common.exception.GlobalErrorCode;
import umc.unimade.global.security.JwtProvider;
import umc.unimade.global.security.JwtToken;
import umc.unimade.global.util.auth.OauthUtil;
import umc.unimade.global.util.auth.SmsUtil;
import umc.unimade.global.util.auth.dto.OauthSignUpDto;
import umc.unimade.global.util.redis.RedisUtil;

import java.util.Map;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class AuthQueryService {
    private final JwtProvider jwtProvider;
    private final SmsUtil smsUtil;
    private final SmsCertification smsCertification;
    private final FcmTokenRepository fcmTokenRepository;


    public ReissueTokenResponseDto reissueToken(HttpServletRequest request, Role role) {
        return ReissueTokenResponseDto.from(jwtProvider.reissueToken(request, role));
    }

    public Boolean authSmsSend(String phone) {
        String code = smsUtil.sendSMS(phone);;
        if(code.isEmpty()){
           throw new CustomException(ErrorCode.SMS_SEND_FAIL);
        }
        smsCertification.createSmsCertification(phone, code);

        return true;
    }

    public Boolean authSmsVerify(SmsVerifyRequestDto smsVerifyRequestDto) {
        if (isVerify(smsVerifyRequestDto)) {
            throw new CustomException(ErrorCode.SMS_VERIFY_FAILED);
        }
        smsCertification.deleteSmsCertification(smsVerifyRequestDto.getPhoneNumber());

        return true;
    }

    @Transactional
    public void updateNotificationToken(Long userId, String token) {

        fcmTokenRepository.saveToken(userId, token);
    }

    private boolean isVerify(SmsVerifyRequestDto smsVerifyRequestDto) {
        return !(smsCertification.hasKey(smsVerifyRequestDto.getPhoneNumber()) &&
                smsCertification.getSmsCertification(smsVerifyRequestDto.getPhoneNumber())
                        .equals(smsVerifyRequestDto.getCertificationNumber()));
    }

}
