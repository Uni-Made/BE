package umc.unimade.domain.noticeBoard.entity;

import jakarta.persistence.*;
import lombok.*;
import umc.unimade.domain.noticeBoard.dto.NoticeBoardRequest;
import umc.unimade.global.common.BaseEntity;

@Entity
@Builder
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Table(name = "notice_board")
public class NoticeBoard extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "notice_board_id", nullable = false)
    private Long id;

    @Column(name = "title", nullable = false)
    private String title;

    @Column(name = "content", nullable = false)
    private String content;

    @Column(name = "view_count", nullable = false)
    private int viewCount; // 조회수

    public void changeNotice(NoticeBoardRequest request) {
        title = request.getTitle();
        content = request.getContent();
    }
    @PrePersist
    public void prePersist() {
        viewCount = 0; // 조회수 기본 0
    }

    public void incrementViewCount() {
        viewCount++;
    }
}
