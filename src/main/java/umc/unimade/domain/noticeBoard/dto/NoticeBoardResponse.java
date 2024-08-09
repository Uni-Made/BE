package umc.unimade.domain.noticeBoard.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import umc.unimade.domain.noticeBoard.entity.NoticeBoard;

import java.time.LocalDateTime;

@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class NoticeBoardResponse {

    private Long id;
    private String title;
    private String content;
    private LocalDateTime createdAt;


    public static NoticeBoardResponse from(NoticeBoard board) {
        return NoticeBoardResponse.builder()
                .id(board.getId())
                .title(board.getTitle())
                .content(board.getContent())
                .createdAt(board.getCreatedAt())
                .build();
    }
}
