package umc.unimade.domain.noticeBoard.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import umc.unimade.domain.noticeBoard.dto.NoticeBoardRequest;
import umc.unimade.domain.noticeBoard.dto.NoticeBoardResponse;
import umc.unimade.domain.noticeBoard.entity.NoticeBoard;
import umc.unimade.domain.noticeBoard.exception.NoticeBoardExceptionHandler;
import umc.unimade.domain.noticeBoard.repository.NoticeBoardRepository;
import umc.unimade.global.common.ErrorCode;

import java.time.LocalDateTime;

@Service
@Transactional
@RequiredArgsConstructor
public class NoticeBoardCommandService {

    private final NoticeBoardRepository noticeBoardRepository;

    public NoticeBoardResponse createNotice(NoticeBoardRequest request) {
        NoticeBoard board = request.toEntity();
        board.setCreatedAt(LocalDateTime.now());
        noticeBoardRepository.save(board);
        return NoticeBoardResponse.from(board);
    }

    public NoticeBoardResponse updateNotice(NoticeBoardRequest request, Long id) {
        NoticeBoard board = noticeBoardRepository.findById(id)
                .orElseThrow(() -> new NoticeBoardExceptionHandler(ErrorCode.NOTICEBOARD_NOT_FOUND));

        board.changeNotice(request);
        return NoticeBoardResponse.from(board);
    }

    public void deleteNotice(Long id) {

        noticeBoardRepository.findById(id)
                .orElseThrow(() -> new NoticeBoardExceptionHandler(ErrorCode.NOTICEBOARD_NOT_FOUND));
        noticeBoardRepository.deleteById(id);
    }
}
