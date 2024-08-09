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
public class NoticeBoardsResponse {

    private Long id;
    private String title;
    private String content;
    private LocalDateTime createdAt;
    private int viewCount;

    public static NoticeBoardsResponse from(NoticeBoard noticeBoard) {
        return NoticeBoardsResponse.builder()
                .id(noticeBoard.getId())
                .title(noticeBoard.getTitle())
                .content(noticeBoard.getContent())
                .createdAt(noticeBoard.getCreatedAt())
                .viewCount(noticeBoard.getViewCount())
                .build();
    }
}
