package umc.unimade.domain.noticeBoard.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Sort;
import org.springframework.web.bind.annotation.*;
import umc.unimade.domain.noticeBoard.dto.NoticeBoardRequest;
import umc.unimade.domain.noticeBoard.dto.NoticeBoardResponse;
import umc.unimade.domain.noticeBoard.dto.NoticeBoardsResponse;
import umc.unimade.domain.noticeBoard.service.NoticeBoardCommandService;
import umc.unimade.domain.noticeBoard.service.NoticeBoardQueryService;
import umc.unimade.global.common.ApiResponse;

@RestController
@RequiredArgsConstructor
@Tag(name = "Admin_Notice", description = "관리자-공지 관련 API")
@RequestMapping("/admin/notice")
public class NoticeBoardController {

    private final NoticeBoardCommandService noticeBoardCommandService;
    private final NoticeBoardQueryService noticeBoardQueryService;

    @Operation(summary = "공지 작성", description = "유저 role이 관리자인 사람만 가능")
    @PostMapping()
    public ApiResponse<NoticeBoardResponse> createNotice(@Valid @RequestBody NoticeBoardRequest request) {
        NoticeBoardResponse response = noticeBoardCommandService.createNotice(request);
        return ApiResponse.onSuccess(response);
    }

    @Operation(summary = "공지 수정", description = "유저 role이 관리자인 사람만 가능")
    @PutMapping("/{noticeBoardId}")
    public ApiResponse<NoticeBoardResponse> updateNotice(@Valid @RequestBody NoticeBoardRequest request,
                                                         @PathVariable Long noticeBoardId) {
        NoticeBoardResponse response = noticeBoardCommandService.updateNotice(request, noticeBoardId);
        return ApiResponse.onSuccess(response);
    }

    @Operation(summary = "공지 리스트 조회", description = "유저 role이 관리자인 사람만 가능")
    @GetMapping()
    public ApiResponse<Page<NoticeBoardsResponse>> getNotices(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "DESC") String sortDirection,
            @RequestParam(required = false) String keyword
    ) {
        Sort.Direction direction = Sort.Direction.fromString(sortDirection.toUpperCase());
        Pageable pageable = PageRequest.of(page, size, Sort.by(direction, "createdAt"));
        Page<NoticeBoardsResponse> responsePage = noticeBoardQueryService.getNotices(pageable, keyword);
        return ApiResponse.onSuccess(responsePage);
    }

    @Operation(summary = "공지 세부 조회", description = "유저 role이 관리자인 사람만 가능")
    @GetMapping("/{noticeBoardId}")
    public ApiResponse<NoticeBoardResponse> getNotice(@PathVariable Long noticeBoardId) {
        NoticeBoardResponse response =noticeBoardQueryService.getNotice(noticeBoardId);
        return ApiResponse.onSuccess(response);
    }

    @Operation(summary = "공지 삭제", description = "유저 role이 관리자인 사람만 가능")
    @DeleteMapping("/{noticeBoardId}")
    public ApiResponse<Object> deleteNotice(@PathVariable Long noticeBoardId) {
        noticeBoardCommandService.deleteNotice(noticeBoardId);
        return ApiResponse.noContent();
    }

}
