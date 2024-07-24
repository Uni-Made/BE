package umc.unimade.global.util.auth;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import io.jsonwebtoken.Claims;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;
import umc.unimade.domain.accounts.entity.Buyer;
import umc.unimade.domain.accounts.entity.Role;
import umc.unimade.domain.accounts.repository.BuyerRepository;
import umc.unimade.global.common.exception.CustomException;
import umc.unimade.global.common.exception.GlobalErrorCode;
import umc.unimade.global.security.JwtProvider;
import umc.unimade.global.util.auth.dto.OauthSignUpDto;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

@Component
@RequiredArgsConstructor
public class OauthUtil {

    @Value("${spring.security.oauth2.client.provider.kakao.token-uri}")
    private String KAKAO_TOKEN_URL;

    @Value("${spring.security.oauth2.client.provider.kakao.user-info-uri}")
    private String KAKAO_USERINFO_URL;

    @Value("${spring.security.oauth2.client.registration.kakao.client-id}")
    private String KAKAO_CLIENT_ID;

    @Value("${spring.security.oauth2.client.registration.kakao.client-secret}")
    private String KAKAO_CLIENT_SECRET;

    @Value("${spring.security.oauth2.client.registration.kakao.redirect-uri}")
    private String KAKAO_REDIRECT_URL;

    @Value("${spring.security.oauth2.client.provider.naver.token-uri}")
    private String NAVER_TOKEN_URL;

    @Value("${spring.security.oauth2.client.provider.naver.user-info-uri}")
    private String NAVER_USERINFO_URL;

    @Value("${spring.security.oauth2.client.registration.naver.client-id}")
    private String NAVER_CLIENT_ID;

    @Value("${spring.security.oauth2.client.registration.naver.client-secret}")
    private String NAVER_CLIENT_SECRET;

    @Value("${spring.security.oauth2.client.registration.naver.redirect-uri}")
    private String NAVER_REDIRECT_URL;

    @Value("${spring.security.oauth2.client.provider.google.token-uri}")
    private String GOOGLE_TOKEN_URL;

    @Value("${spring.security.oauth2.client.provider.google.user-info-uri}")
    private String GOOGLE_USERINFO_URL;

    @Value("${spring.security.oauth2.client.registration.google.client-id}")
    private String GOOGLE_CLIENT_ID;

    @Value("${spring.security.oauth2.client.registration.google.client-secret}")
    private String GOOGLE_CLIENT_SECRET;

    @Value("${spring.security.oauth2.client.registration.google.redirect-uri}")
    private String GOOGLE_REDIRECT_URL;

    private final RestTemplate restTemplate;
    private final JwtProvider jwtProvider;
    private final BuyerRepository buyerRepository;

    public String getKakaoAccessToken(String authCode) {
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.add("Content-type", "application/x-www-form-urlencoded;charset=utf-8");

        MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
        params.add("grant_type", "authorization_code");
        params.add("client_id", KAKAO_CLIENT_ID);
        params.add("client_secret", KAKAO_CLIENT_SECRET);
        params.add("redirect_uri", KAKAO_REDIRECT_URL);
        params.add("code", authCode);

        HttpEntity<MultiValueMap<String, String>> tokenRequest = new HttpEntity<>(params, httpHeaders);
        ResponseEntity<String> response = restTemplate.exchange(
                KAKAO_TOKEN_URL,
                HttpMethod.POST,
                tokenRequest,
                String.class
        );

        JsonObject jsonObject = JsonParser.parseString(Objects.requireNonNull(response.getBody())).getAsJsonObject();
        return jsonObject.get("access_token").getAsString();
    }

    public String getNaverAccessToken(String authCode) {
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.add("Content-type", "application/x-www-form-urlencoded;charset=utf-8");

        MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
        params.add("grant_type", "authorization_code");
        params.add("client_id", NAVER_CLIENT_ID);
        params.add("client_secret", NAVER_CLIENT_SECRET);
        params.add("redirect_uri", NAVER_REDIRECT_URL);
        params.add("code", authCode);

        HttpEntity<MultiValueMap<String, String>> tokenRequest = new HttpEntity<>(params, httpHeaders);
        ResponseEntity<String> response = restTemplate.exchange(
                NAVER_TOKEN_URL,
                HttpMethod.POST,
                tokenRequest,
                String.class
        );

        JsonObject jsonObject = JsonParser.parseString(Objects.requireNonNull(response.getBody())).getAsJsonObject();
        return jsonObject.get("access_token").getAsString();
    }

    public String getGoogleAccessToken(String authCode) {
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.add("Content-type", "application/x-www-form-urlencoded;charset=utf-8");

        MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
        params.add("grant_type", "authorization_code");
        params.add("client_id", GOOGLE_CLIENT_ID);
        params.add("client_secret", GOOGLE_CLIENT_SECRET);
        params.add("redirect_uri", GOOGLE_REDIRECT_URL);
        params.add("code", authCode);

        HttpEntity<MultiValueMap<String, String>> tokenRequest = new HttpEntity<>(params, httpHeaders);
        ResponseEntity<String> response = restTemplate.exchange(
                GOOGLE_TOKEN_URL,
                HttpMethod.POST,
                tokenRequest,
                String.class
        );

        JsonObject jsonObject = JsonParser.parseString(Objects.requireNonNull(response.getBody())).getAsJsonObject();
        return jsonObject.get("access_token").getAsString();
    }

    public OauthSignUpDto getKakaoUserInfo(String accessToken) {
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.add("Authorization", "Bearer " + accessToken);
        httpHeaders.add("Content-type", "application/x-www-form-urlencoded;charset=utf-8");

        HttpEntity<MultiValueMap<String, String>> profileRequest = new HttpEntity<>(httpHeaders);
        ResponseEntity<String> response = restTemplate.exchange(
                KAKAO_USERINFO_URL,
                HttpMethod.POST,
                profileRequest,
                String.class
        );

        JsonObject element = JsonParser.parseString(response.getBody()).getAsJsonObject();
        System.err.println("카카오 정보 : " + element);
        //System.err.println(element.getAsJsonObject("kakao_account").get("email").getAsString());
        return OauthSignUpDto.builder()
                .socialId(element.get("id").getAsString())
                .socialEmail(element.getAsJsonObject("kakao_account").get("email").getAsString())
                .socialName(element.getAsJsonObject("properties").get("nickname").getAsString())
                .profileImage(element.getAsJsonObject("properties").get("profile_image").getAsString())
                .build();
    }

    public OauthSignUpDto getNaverUserInfo(String accessToken) {
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.add("Authorization", "Bearer " + accessToken);
        httpHeaders.add("Content-type", "application/x-www-form-urlencoded;charset=utf-8");

        HttpEntity<MultiValueMap<String, String>> profileRequest = new HttpEntity<>(httpHeaders);
        ResponseEntity<String> response = restTemplate.exchange(
                NAVER_USERINFO_URL,
                HttpMethod.POST,
                profileRequest,
                String.class
        );

        JsonObject element = JsonParser.parseString(response.getBody()).getAsJsonObject();
        System.err.println("네이버 정보 : " + element);
        //네이버 정보 : {"resultcode":"00","message":"success","response":{"id":"GL9ATxEhX7p9XImQHK9ZfUt2d8Zz5JH7z7_HuzheKaQ","profile_image":"https://ssl.pstatic.net/static/pwe/address/img_profile.png","email":"rokmcp150@naver.com","name":"박민기"}}
        return OauthSignUpDto.builder()
                .socialId(element.getAsJsonObject("response").get("id").getAsString())
                .socialEmail(element.getAsJsonObject("response").get("email").getAsString())
                .socialName(element.getAsJsonObject("response").get("name").getAsString())
                .profileImage(element.getAsJsonObject("response").get("profile_image").getAsString())
                .build();
    }

    public OauthSignUpDto getGoogleUserInfo(String accessToken) {
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.add("Authorization", "Bearer " + accessToken);
        httpHeaders.add("Content-type", "application/x-www-form-urlencoded;charset=utf-8");

        HttpEntity<MultiValueMap<String, String>> profileRequest = new HttpEntity<>(httpHeaders);
        System.err.println(profileRequest);
        ResponseEntity<String> response = restTemplate.exchange(
                GOOGLE_USERINFO_URL,
                HttpMethod.GET,
                profileRequest,
                String.class
        );

        System.err.println("response에 대한 "+response.getBody());
        JsonObject element = JsonParser.parseString(response.getBody()).getAsJsonObject();
        System.err.println("구글 정보 "+ element);
        //구글 정보 {"sub":"106483083230577695980","name":"민기박","given_name":"박","family_name":"민기","picture":"https://lh3.googleusercontent.com/a/ACg8ocIrmKvc7O8uiw33WS_nzxEoF-sL996XpX4rn5n3MhNE77fPVA=s96-c","email":"rokmck156@gmail.com","email_verified":true}
        return OauthSignUpDto.builder()
                .socialId(element.get("sub").getAsString())
                .socialEmail(element.get("email").getAsString())
                .socialName(element.get("name").getAsString())
                .profileImage(element.get("picture").getAsString())
                .build();
    }

    public Map<String, String> userInfo() {
        final Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String userId = authentication.getName();
        String userRole = authentication.getAuthorities().toString().substring(6, authentication.getAuthorities().toString().length()-1);

        Map<String, String> info = new HashMap<>();
        info.put("userId", userId);
        info.put("role", userRole);
        return info;
    }
}
