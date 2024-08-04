package umc.unimade.domain.accounts.dto;
//social name과 email을 받아오는 dto

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@Builder
public class SignUpResponseDto {
    private Integer status;
    private String socialName;
    private String email;
    private String socialId;

    public SignUpResponseDto(Integer status, String socialName, String email, String socialId) {
        this.status = status;
        this.socialName = socialName;
        this.email = email;
        this.socialId = socialId;
    }

    public static SignUpResponseDto from(Integer status, String socialName, String email, String socialId) {
        return SignUpResponseDto.builder()
                .status(status)
                .socialName(socialName)
                .email(email)
                .socialId(socialId)
                .build();
    }


}
