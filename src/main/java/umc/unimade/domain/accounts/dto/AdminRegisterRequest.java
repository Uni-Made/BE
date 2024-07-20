package umc.unimade.domain.accounts.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class AdminRegisterRequest {

    @Schema(description = "사유", example = "이상한데요")
    private String reason;
}