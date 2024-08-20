package umc.unimade.domain.accounts.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class ProfileResponseDto {
    private String profileImage;

    public static ProfileResponseDto of(String profileImage) {
        return new ProfileResponseDto(profileImage);
    }
}
