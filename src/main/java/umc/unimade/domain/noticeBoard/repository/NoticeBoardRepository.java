package umc.unimade.domain.noticeBoard.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import umc.unimade.domain.noticeBoard.entity.NoticeBoard;

public interface NoticeBoardRepository extends JpaRepository<NoticeBoard, Long> {
}
