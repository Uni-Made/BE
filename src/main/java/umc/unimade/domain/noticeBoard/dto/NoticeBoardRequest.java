package umc.unimade.domain.noticeBoard.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import umc.unimade.domain.noticeBoard.entity.NoticeBoard;

@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class NoticeBoardRequest {

    @NotBlank(message = "[ERROR] 제목은 필수입니다.")
    @Schema(description = "title", example = "[공지] 입금 관련 안내")
    private String title;
    @NotBlank(message = "[ERROR] 내용은 필수입니다.")
    @Schema(description = "content", example = "내용내용")
    private String content;

    public NoticeBoard toEntity() {
        return NoticeBoard.builder()
                .title(title)
                .content(content)
                .build();
    }
}
