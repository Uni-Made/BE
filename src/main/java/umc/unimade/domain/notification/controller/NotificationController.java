package umc.unimade.domain.notification.controller;


import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import umc.unimade.domain.notification.service.NotificationService;
import umc.unimade.domain.orders.repository.OrderRepository;

@RestController
@RequestMapping("/api/notifications")
@RequiredArgsConstructor
public class  NotificationController {
    private final NotificationService notificationService;
    private final OrderRepository orderRepository;

}


