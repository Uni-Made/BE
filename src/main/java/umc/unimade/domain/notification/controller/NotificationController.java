package umc.unimade.domain.notification.controller;


import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import umc.unimade.domain.accounts.entity.Buyer;
import umc.unimade.domain.accounts.exception.UserExceptionHandler;
import umc.unimade.domain.notification.dto.NotificationListResponse;
import umc.unimade.domain.notification.service.NotificationService;
import umc.unimade.global.common.ApiResponse;
import umc.unimade.global.common.ErrorCode;
import umc.unimade.global.security.LoginBuyer;

@RestController
@RequestMapping("/api/notifications")
@RequiredArgsConstructor
public class  NotificationController {
    private final NotificationService notificationService;

    @Tag(name = "Notification")
    @Operation(summary = "알림 목록 불러오기")
    @GetMapping("/list")
    public ResponseEntity<ApiResponse<NotificationListResponse>> getNotificationList(@LoginBuyer Buyer buyer,
                                                                                           @RequestParam(required = false) Long cursor,
                                                                                           @RequestParam int pageSize) {
        try{
            return ResponseEntity.ok(ApiResponse.onSuccess(notificationService.getNotificationList(buyer, cursor, pageSize)));
        }catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ApiResponse.onFailure(HttpStatus.BAD_REQUEST.name(), e.getMessage()));
        } catch (UserExceptionHandler e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(ApiResponse.onFailure(ErrorCode.BUYER_NOT_FOUND.getCode(), ErrorCode.BUYER_NOT_FOUND.getMessage()));
        }
    }

}


