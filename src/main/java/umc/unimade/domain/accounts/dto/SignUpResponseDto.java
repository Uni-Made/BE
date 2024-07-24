package umc.unimade.domain.accounts.dto;
//social name과 email을 받아오는 dto

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class SignUpResponseDto {
    private Integer status;
    private String socialName;
    private String email;
    private String socialId;

}
