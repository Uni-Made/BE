package umc.unimade.domain.accounts.controller;

import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import umc.unimade.domain.accounts.dto.*;
import umc.unimade.domain.accounts.entity.Provider;
import umc.unimade.domain.accounts.entity.Role;
import umc.unimade.domain.accounts.service.AuthQueryService;
import umc.unimade.domain.accounts.service.BuyerQueryService;
import umc.unimade.global.common.ApiResponse;

@RestController
@RequiredArgsConstructor
@RequestMapping("/auth")
public class AuthController {
    private final AuthQueryService authQueryService;

    // 소셜 로그인 - 구매자(buyer)
    @PostMapping("/buyers/kakao")
    public ApiResponse<Object> byKakao(@RequestParam("code") String code, @RequestParam String fcmToken) {
        System.err.println(code);
        return ApiResponse.onSuccess(authQueryService.socialLogin(code, Provider.KAKAO,fcmToken));
    }

    @PostMapping("/buyers/naver")
    public ApiResponse<Object> byNaver(@RequestParam("code") String code,@RequestParam String fcmToken) {
        return ApiResponse.onSuccess(authQueryService.socialLogin(code, Provider.NAVER,fcmToken));
    }

    @PostMapping("/buyers/google")
    public ApiResponse<Object> byGoogle(@RequestParam("code") String code,@RequestParam String fcmToken) {
        return ApiResponse.onSuccess(authQueryService.socialLogin(code, Provider.GOOGLE,fcmToken));
    }

    @PostMapping("/buyers/reissue")
    public ApiResponse<ReissueTokenResponseDto> reissueBuyer(HttpServletRequest request) {
        return ApiResponse.onSuccess(authQueryService.reissueToken(request, Role.BUYER));
    }

    @PostMapping("/logout")
    public ApiResponse<Boolean> logoutCustomer(
    ) {
        return ApiResponse.onSuccess(authQueryService.logout());
    }

    @PostMapping("/buyers/signup")
    public ApiResponse<SignInResponseDto> buyerSignUp(
            @RequestBody SignUpReqeustDto signUpReqeustDto
            ) {
        return ApiResponse.onSuccess(authQueryService.buyerSignUp(signUpReqeustDto, Role.BUYER));
    }

    @PostMapping("/seller/signup")
    public ApiResponse<SignInResponseDto> sellerSignUp(
            @RequestBody SignUpReqeustDto signUpReqeustDto
    ) {
        return ApiResponse.onSuccess(authQueryService.sellerSignUp(signUpReqeustDto, Role.SELLER));
    }

    @PostMapping("/seller/signin")
    public ApiResponse<SignInResponseDto> sellerSignIn(
            @RequestBody SignInRequestDto signInRequestDto
    ) {
        return ApiResponse.onSuccess(authQueryService.sellerSignIn(signInRequestDto, Role.SELLER));
    }

    @PostMapping("/sms")
    public ApiResponse<String> authSmsSend(
            @RequestParam("phone") String phone
    ) {
        return ApiResponse.onSuccess(authQueryService.authSmsSend(phone));
    }

    @PostMapping("/sms/verify")
    public ApiResponse<Boolean> authSmsVerify(
            @RequestBody SmsVerifyRequestDto smsVerifyRequestDto
    ) {
        return ApiResponse.onSuccess(authQueryService.authSmsVerify(smsVerifyRequestDto));
    }












}
