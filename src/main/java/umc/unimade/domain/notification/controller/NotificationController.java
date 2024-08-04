package umc.unimade.domain.notification.controller;


import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import umc.unimade.domain.notification.service.NotificationService;
import umc.unimade.domain.products.exception.ProductsExceptionHandler;
import umc.unimade.global.common.ApiResponse;
import umc.unimade.global.common.ErrorCode;

@RestController
@RequestMapping("/api/notification")
@RequiredArgsConstructor
public class NotificationController {

    private final NotificationService notificationService;

    @Tag(name = "Notification", description = "알림 API")
    @Operation(summary = "구매자 FCM 토큰 저장하기")
    @PostMapping("/{buyerId}/new")
    public ResponseEntity<ApiResponse<Void>> saveBuyerNotification(@PathVariable Long buyerId, @RequestBody String token){
        try {
            notificationService.saveBuyerNotification(buyerId,token);
            return ResponseEntity.ok(ApiResponse.onSuccess(null));
        }
        catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ApiResponse.onFailure(HttpStatus.BAD_REQUEST.name(), e.getMessage()));
        }
    }
}
