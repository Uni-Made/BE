package umc.unimade.domain.notification.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.firebase.messaging.FirebaseMessaging;
import com.google.firebase.messaging.Message;
import lombok.RequiredArgsConstructor;
import org.springframework.context.event.EventListener;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import umc.unimade.domain.accounts.entity.Buyer;
import umc.unimade.domain.accounts.exception.UserExceptionHandler;
import umc.unimade.domain.accounts.repository.BuyerRepository;
import umc.unimade.domain.notification.dto.NotificationListResponse;
import umc.unimade.domain.notification.dto.NotificationRequest;
import umc.unimade.domain.notification.entity.BuyerNotification;
import umc.unimade.domain.notification.events.AnswerPostedEvent;
import umc.unimade.domain.notification.events.OrderCancelledEvent;
import umc.unimade.domain.notification.events.OrderRequestEvent;
import umc.unimade.domain.notification.events.ReviewRequestEvent;
import umc.unimade.domain.notification.repository.BuyerNotificationRepository;
import umc.unimade.domain.orders.entity.OrderStatus;
import umc.unimade.domain.orders.entity.Orders;
import umc.unimade.domain.orders.exception.OrderExceptionHandler;
import umc.unimade.domain.orders.repository.OrderRepository;
import umc.unimade.domain.products.dto.ProductsListResponse;
import umc.unimade.domain.qna.entity.Questions;
import umc.unimade.domain.qna.exception.QnAExceptionHandler;
import umc.unimade.domain.qna.repository.QuestionsRepository;
import umc.unimade.global.common.ErrorCode;
import umc.unimade.global.util.redis.RedisUtil;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.List;

@RequiredArgsConstructor
@Service
public class BuyerNotificationService {
    private final BuyerRepository buyerRepository;
    private final OrderRepository orderRepository;
    private final QuestionsRepository questionsRepository;
    private final BuyerNotificationRepository buyerNotificationRepository;
    private final RedisUtil redisUtil;
    private final ObjectMapper objectMapper;


    /*구매자 알림*/
    public NotificationListResponse getNotificationList(Buyer buyer, Long cursor, int pageSize){
        Pageable pageable = PageRequest.of(0, pageSize);
        List<BuyerNotification> notifications;
        if (cursor == null) {
            notifications = buyerNotificationRepository.findByBuyerOrderByCreatedAtDesc(buyer, pageable);
        } else {
            notifications = buyerNotificationRepository.findByBuyerAndIdLessThanOrderByCreatedAtDesc(buyer, cursor, pageable);
        }

        Long nextCursor = notifications.isEmpty()?null : notifications.get(notifications.size() - 1).getId();
        Boolean isLast = notifications.size()<pageSize;

        return NotificationListResponse.from(notifications, nextCursor, isLast);

    }

    //구매 완료 알림
    @Async
    @EventListener
    public void handleOrderCompletedEvent(OrderRequestEvent event) {
        Buyer buyer = findBuyerById(event.getUserId());
        Orders order = findOrderById(event.getOrderId());
        sendOrderCompleteNotification(buyer,order);
    }


    @Async
    @EventListener
    public void handleOrderCancelledEvent(OrderCancelledEvent event) {
        Buyer buyer = findBuyerById(event.getUserId());
        Orders order = findOrderById(event.getOrderId());
        sendOrderCancelledNotification(buyer,order);
    }
    // 입금 안내 알림
    @Scheduled(cron = "0 0 10 * * ?")
    public void sendPendingPaymentReminders() {
        LocalDate currentDate = LocalDate.now();
        List<Orders> pendingOrders = orderRepository.findByStatusAndCreatedAtBetween(
                OrderStatus.PENDING, currentDate.minusDays(3).atStartOfDay(), currentDate.atStartOfDay());

        for (Orders order : pendingOrders) {
            Buyer buyer = order.getBuyer();
            sendPaymentReminderNotification(buyer,order);
        }
    }

    // 수령 당일 알림
    @Scheduled(cron = "0 0 10 * * ?")
    public void sendPickupReminders() {
        LocalDate currentDate = LocalDate.now();
        List<Orders> orders = orderRepository.findByStatusAndPickupDate(OrderStatus.PAID, currentDate);

        for (Orders order : orders) {
            Buyer buyer = order.getBuyer();
            sendPickupNotification(buyer,order);
        }
    }

    // QNA 답변 알림
    @Async
    @EventListener
    public void handleAnswerPostedEvent(AnswerPostedEvent event) {
        Buyer buyer = findBuyerById(event.getUserId());
        Questions question = findQuestionById(event.getQuestionId());
        sendAnswerNotification(buyer,question);
    }

    // 리뷰 작성 요청 알람
    @Async
    @EventListener
    public void handleReviewRequestEvent(ReviewRequestEvent event) {
        Buyer buyer = findBuyerById(event.getUserId());
        Orders order = findOrderById(event.getOrderId());
        sendReviewReminderNotification(buyer,order);
    }

    // 리뷰 생성 후 15일 지나면 삭제
    @Scheduled(cron = "0 0 9 * * ?")
    public void deleteOldNotifications() {
        LocalDateTime timeLimit = LocalDateTime.now().minusDays(15);
        buyerNotificationRepository.deleteAllByCreatedAtBefore(timeLimit);
    }

    private void sendOrderCompleteNotification(Buyer buyer,Orders order){
        sendNotification(buyer.getSocialId(),new NotificationRequest("구매 완료 알림",order.getProduct().getName(),String.valueOf(buyer.getId())));
    }

    private void sendOrderCancelledNotification(Buyer buyer, Orders order){
        sendNotification(buyer.getSocialId(),new NotificationRequest("입금 미완료 구매 취소 알림", order.getProduct().getName(), String.valueOf(buyer.getId())));
    }

    private void sendPaymentReminderNotification(Buyer buyer, Orders order) {
        long daysLeft = ChronoUnit.DAYS.between(order.getCreatedAt().toLocalDate(), LocalDate.now());
        String body = String.format(order.getProduct().getName(),"주문 후 %d일이 지났습니다. 입금을 완료해 주세요.", daysLeft);
        sendNotification(buyer.getSocialId(), new NotificationRequest("입금 알림", body, String.valueOf(buyer.getId())));
    }

    private void sendPickupNotification(Buyer buyer,Orders order) {
        sendNotification(buyer.getSocialId(), new NotificationRequest("수령 알림",order.getProduct().getName(), String.valueOf(buyer.getId())));
    }

    private void sendAnswerNotification(Buyer buyer, Questions question) {
        sendNotification(buyer.getSocialId(), new NotificationRequest("답변 등록 알림",question.getTitle(),String.valueOf(question.getProduct().getId())));
    }

    private void sendReviewReminderNotification(Buyer buyer,Orders order) {
        ProductsListResponse.ProductInfo productInfo = ProductsListResponse.ProductInfo.from(order.getProduct());
        try {
            String body = objectMapper.writeValueAsString(productInfo);

            NotificationRequest notificationRequest = new NotificationRequest(
                    "리뷰 작성 알림",
                    body,
                    String.valueOf(order.getId())
            );

            new java.util.Timer().schedule(
                    new java.util.TimerTask() {
                        @Override
                        public void run() {
                            sendNotification(buyer.getSocialId(), notificationRequest);
                        }
                    },
                    5 * 60 * 1000 // 5 minutes delay
            );
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void sendNotification(String socialId, NotificationRequest notificationRequest) {
        try {
            String fcmToken = redisUtil.getFCMToken(socialId);
            Buyer buyer = findBuyerBySocialId(socialId);
            if (fcmToken != null) {
                Message message = Message.builder()
                        .putData("title", notificationRequest.getTitle())
                        .putData("body", notificationRequest.getBody())
                        .putData("id", notificationRequest.getId())
                        .setToken(fcmToken)
                        .build();
                FirebaseMessaging.getInstance().sendAsync(message).get();
                BuyerNotification notification = BuyerNotification.builder()
                        .buyer(buyer)
                        .title(notificationRequest.getTitle())
                        .body(notificationRequest.getBody())
                        .extraId(notificationRequest.getId())
                        .build();
                buyerNotificationRepository.save(notification);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private Buyer findBuyerById(Long buyerId) {
        return buyerRepository.findById(buyerId)
                .orElseThrow(() -> new UserExceptionHandler(ErrorCode.BUYER_NOT_FOUND));
    }

    private Buyer findBuyerBySocialId(String email){
        return buyerRepository.findBySocialId(email)
                .orElseThrow(() -> new UserExceptionHandler(ErrorCode.BUYER_NOT_FOUND));
    }


    private Orders findOrderById(Long orderId){
        return orderRepository.findById(orderId)
                .orElseThrow(()-> new OrderExceptionHandler(ErrorCode.ORDER_NOT_FOUND));
    }

    private Questions findQuestionById(Long questionId){
        return questionsRepository.findById(questionId)
                .orElseThrow(()-> new QnAExceptionHandler(ErrorCode.QNA_NOT_FOUND));
    }

}
