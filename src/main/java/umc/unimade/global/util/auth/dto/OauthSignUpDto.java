package umc.unimade.global.util.auth.dto;

import lombok.Builder;
import lombok.Getter;

@Getter
public class OauthSignUpDto {
    private String socialId;
    private String socialEmail;
    private String socialName;
    private String profileImage;

    @Builder
    public OauthSignUpDto(String socialId, String socialEmail, String socialName, String profileImage) {
        this.socialId = socialId;
        this.socialEmail = socialEmail;
        this.socialName = socialName;
        this.profileImage = profileImage;
    }
}
