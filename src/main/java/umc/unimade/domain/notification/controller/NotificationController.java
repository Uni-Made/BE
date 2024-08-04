package umc.unimade.domain.notification.controller;


import lombok.RequiredArgsConstructor;
import io.swagger.v3.oas.annotations.Operation;
import org.springframework.web.bind.annotation.*;
import umc.unimade.domain.notification.dto.NotificationRequest;
import umc.unimade.domain.notification.service.NotificationService;
import umc.unimade.global.common.ApiResponse;

@RestController
@RequestMapping("/api/notifications")
@RequiredArgsConstructor
public class NotificationController {
    private final NotificationService notificationService;

    @Operation(summary = "알림 보내기", description = "구매자 FCM 푸시 알림 전송")
    @PostMapping("/send/{buyerId}")
    public ApiResponse<Void> sendBuyerNotification(@RequestBody NotificationRequest notificationRequest, @PathVariable Long buyerId) {
        notificationService.sendBuyerNotification(buyerId, notificationRequest);
        return ApiResponse.onSuccess(null);
    }

    @Operation(summary = "알림 보내기", description = "판매자 FCM 푸시 알림 전송")
    @PostMapping("/send/{sellerId}")
    public ApiResponse<Void> sendSellerNotification(@RequestBody NotificationRequest notificationRequest, @PathVariable Long sellerId) {
        notificationService.sendSellerNotification(sellerId, notificationRequest);
        return ApiResponse.onSuccess(null);
    }

}
