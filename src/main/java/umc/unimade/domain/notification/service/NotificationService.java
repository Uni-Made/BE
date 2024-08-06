package umc.unimade.domain.notification.service;

import com.google.firebase.messaging.FirebaseMessaging;
import com.google.firebase.messaging.Message;
import lombok.RequiredArgsConstructor;
import org.springframework.context.event.EventListener;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import umc.unimade.domain.accounts.entity.Buyer;
import umc.unimade.domain.accounts.entity.Seller;
import umc.unimade.domain.accounts.exception.UserExceptionHandler;
import umc.unimade.domain.accounts.repository.BuyerRepository;
import umc.unimade.domain.notification.dto.NotificationRequest;
import umc.unimade.domain.notification.events.AnswerPostedEvent;
import umc.unimade.domain.notification.events.OrderRequestEvent;
import umc.unimade.domain.notification.events.ReviewRequestEvent;
import umc.unimade.domain.notification.repository.BuyerNotificationRepository;
import umc.unimade.domain.orders.entity.OrderStatus;
import umc.unimade.domain.orders.entity.Orders;
import umc.unimade.domain.orders.repository.OrderRepository;
import umc.unimade.domain.products.entity.Products;
import umc.unimade.domain.products.exception.ProductsExceptionHandler;
import umc.unimade.domain.products.repository.ProductRepository;
import umc.unimade.global.common.ErrorCode;
import umc.unimade.global.util.redis.RedisUtil;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@RequiredArgsConstructor
@Service
public class NotificationService {
    private final BuyerRepository buyerRepository;
    private final OrderRepository orderRepository;
    private final ProductRepository productRepository;
    private final BuyerNotificationRepository buyerNotificationRepository;
    private final RedisUtil redisUtil;

    /*판매자 알림*/
    @Async
    public void sendSellerNotification(Seller seller, NotificationRequest notificationRequest) {
        sendNotification(seller.getEmail(), notificationRequest);
    }

    /*구매자 알림*/

    // TO DO : 프론트와 상의해서 알림을 저장해뒀다가 목록을 반환해야하는지 결정

    //구매 완료 알림
    @Async
    @EventListener
    public void handleOrderCompletedEvent(OrderRequestEvent event) {
        Buyer buyer = findBuyerById(event.getUserId());
        sendOrderCompleteNotification(buyer);
    }

    // 입금 안내 알림
    @Scheduled(cron = "0 0 10 * * ?")
    public void sendPendingPaymentReminders() {
        LocalDate currentDate = LocalDate.now();
        List<Orders> pendingOrders = orderRepository.findByStatusAndCreatedAtBetween(
                OrderStatus.PENDING, currentDate.minusDays(3).atStartOfDay(), currentDate.atStartOfDay());

        for (Orders order : pendingOrders) {
            Buyer buyer = order.getBuyer();
            sendPaymentReminderNotification(buyer);
        }
    }

    // 수령 당일 알림
    @Scheduled(cron = "0 0 10 * * ?")
    public void sendPickupReminders() {
        LocalDate currentDate = LocalDate.now();
        List<Orders> orders = orderRepository.findByStatusAndPickupDate(OrderStatus.PAID, currentDate);

        for (Orders order : orders) {
            Buyer buyer = order.getBuyer();
            sendPickupNotification(buyer);
        }
    }

    // QNA 답변 알림
    @Async
    @EventListener
    public void handleAnswerPostedEvent(AnswerPostedEvent event) {
        Buyer buyer = findBuyerById(event.getUserId());
        Products product = findProductById(event.getProductId());
        sendAnswerNotification(buyer,product);
    }

    // 리뷰 작성 요청 알람
    @Async
    @EventListener
    public void handleReviewRequestEvent(ReviewRequestEvent event) {
        Buyer buyer = findBuyerById(event.getUserId());
        Long orderId = event.getOrder().getId();
        sendReviewReminderNotification(buyer,orderId);
    }

    // 리뷰 생성 후 3일 지나면 삭제
//    @Scheduled(cron = "0 0 9 * * ?")
//    public void deleteOldNotifications() {
//        LocalDateTime timeLimit = LocalDateTime.now().minusDays(3);
//        buyerNotificationRepository.deleteAllByCreatedAtBefore(timeLimit);
//    }

    private void sendOrderCompleteNotification(Buyer buyer){
        sendNotification(buyer.getEmail(),new NotificationRequest("구매 완료 알림",String.valueOf(buyer.getId())));
    }
    private void sendPaymentReminderNotification(Buyer buyer) {
        sendNotification(buyer.getEmail(), new NotificationRequest("입금 알림",String.valueOf(buyer.getId())));
    }

    private void sendPickupNotification(Buyer buyer) {
        sendNotification(buyer.getEmail(), new NotificationRequest("수령 알림",String.valueOf(buyer.getId())));
    }

    private void sendAnswerNotification(Buyer buyer, Products product) {
        sendNotification(buyer.getEmail(), new NotificationRequest("답변 등록 알림",String.valueOf(product.getId())));
    }

    private void sendReviewReminderNotification(Buyer buyer,Long orderId) {
        NotificationRequest notificationRequest = new NotificationRequest("리뷰 작성 알림", String.valueOf(orderId));
        new java.util.Timer().schedule(
                new java.util.TimerTask() {
                    @Override
                    public void run() {
                        sendNotification(buyer.getEmail(), notificationRequest);
                    }
                },
                5 * 60 * 1000 // 5 minutes delay
        );
    }

    private void sendNotification(String email, NotificationRequest notificationRequest) {
        try {
            String fcmToken = redisUtil.getFCMToken(email);
            Buyer buyer = findBuyerByEmail(email);
            if (fcmToken != null) {
                Message message = Message.builder()
                        .putData("title", notificationRequest.getTitle())
                        .putData("body", notificationRequest.getBody())
                        .setToken(fcmToken)
                        .build();
                FirebaseMessaging.getInstance().sendAsync(message).get();
//                BuyerNotification notification = BuyerNotification.builder()
//                        .buyer(buyer)
//                        .title(notificationRequest.getTitle())
//                        .body(notificationRequest.getBody())
//                        .build();
//                buyerNotificationRepository.save(notification);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private Buyer findBuyerById(Long buyerId) {
        return buyerRepository.findById(buyerId)
                .orElseThrow(() -> new UserExceptionHandler(ErrorCode.BUYER_NOT_FOUND));
    }

    private Buyer findBuyerByEmail(String email){
        return buyerRepository.findByEmail(email)
                .orElseThrow(() -> new UserExceptionHandler(ErrorCode.BUYER_NOT_FOUND));
    }

    private Products findProductById(Long productId){
        return productRepository.findById(productId)
                .orElseThrow(()-> new ProductsExceptionHandler(ErrorCode.PRODUCT_NOT_FOUND));
    }

}
