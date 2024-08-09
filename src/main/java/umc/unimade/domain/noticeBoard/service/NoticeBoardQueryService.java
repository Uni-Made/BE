package umc.unimade.domain.noticeBoard.service;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import umc.unimade.domain.noticeBoard.dto.NoticeBoardResponse;
import umc.unimade.domain.noticeBoard.dto.NoticeBoardsResponse;
import umc.unimade.domain.noticeBoard.entity.NoticeBoard;
import umc.unimade.domain.noticeBoard.exception.NoticeBoardExceptionHandler;
import umc.unimade.domain.noticeBoard.repository.NoticeBoardRepository;
import umc.unimade.global.common.ErrorCode;

@Service
@RequiredArgsConstructor
public class NoticeBoardQueryService {

    private final NoticeBoardRepository noticeBoardRepository;

    @Transactional
    public NoticeBoardResponse getNotice(Long noticeBoardId) {

        NoticeBoard noticeBoard = noticeBoardRepository.findById(noticeBoardId)
                .orElseThrow(() -> new NoticeBoardExceptionHandler(ErrorCode.NOTICEBOARD_NOT_FOUND));

        // 조회수 증가
        noticeBoard.incrementViewCount();
        noticeBoardRepository.save(noticeBoard);

        return NoticeBoardResponse.from(noticeBoard);
    }

    @Transactional(readOnly = true)
    public Page<NoticeBoardsResponse> getNotices(Pageable pageable) {
        Page<NoticeBoard> noticeBoards = noticeBoardRepository.findAll(pageable);
        return noticeBoards.map(NoticeBoardsResponse::from);
    }
}
