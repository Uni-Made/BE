package umc.unimade.domain.notification.service;

import com.google.firebase.messaging.FirebaseMessaging;
import com.google.firebase.messaging.Message;
import lombok.RequiredArgsConstructor;
import org.springframework.context.event.EventListener;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import umc.unimade.domain.accounts.entity.Seller;
import umc.unimade.domain.accounts.exception.UserExceptionHandler;
import umc.unimade.domain.accounts.repository.SellerRepository;
import umc.unimade.domain.notification.dto.NotificationRequest;
import umc.unimade.domain.notification.dto.SellerNotificationListResponse;
import umc.unimade.domain.notification.entity.SellerNotification;
import umc.unimade.domain.notification.events.OrderedEvent;
import umc.unimade.domain.notification.events.QuestionPostedEvent;
import umc.unimade.domain.notification.events.ReviewPostedEvent;
import umc.unimade.domain.notification.repository.SellerNotificationRepository;
import umc.unimade.domain.orders.entity.OrderStatus;
import umc.unimade.domain.orders.entity.Orders;
import umc.unimade.domain.orders.repository.OrderRepository;
import umc.unimade.domain.products.entity.PickupOption;
import umc.unimade.domain.qna.entity.Questions;
import umc.unimade.domain.qna.exception.QnAExceptionHandler;
import umc.unimade.domain.qna.repository.QuestionsRepository;
import umc.unimade.domain.review.entity.Review;
import umc.unimade.domain.review.exception.ReviewExceptionHandler;
import umc.unimade.domain.review.repository.ReviewRepository;
import umc.unimade.global.common.ErrorCode;
import umc.unimade.global.util.redis.RedisUtil;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class SellerNotificationService {

    private final RedisUtil redisUtil;
    private final SellerRepository sellerRepository;
    private final SellerNotificationRepository sellerNotificationRepository;
    private final OrderRepository orderRepository;
    private final QuestionsRepository questionsRepository;
    private final ReviewRepository reviewRepository;

    // 구매 알림
    @Async
    @EventListener
    public void handleOrderedEvent(OrderedEvent event) {
        Seller seller = sellerRepository.findById(event.getSellerId())
                .orElseThrow(() -> new UserExceptionHandler(ErrorCode.SELLER_NOT_FOUND));
        Orders order = orderRepository.findById(event.getOrderId())
                .orElseThrow(() -> new UserExceptionHandler(ErrorCode.ORDER_NOT_FOUND));

        sendSellerNotification(seller.getEmail(), new NotificationRequest("상품 구매 알림", order.getProduct().getName(), event.getOrderId().toString()));
    }

    // qna 등록 알림
    @Async
    @EventListener
    public void handleQuestionPostedEvent(QuestionPostedEvent event) {
        Seller seller = sellerRepository.findById(event.getSellerId())
                .orElseThrow(() -> new UserExceptionHandler(ErrorCode.SELLER_NOT_FOUND));
        Questions question = questionsRepository.findById(event.getQuestionId())
                .orElseThrow(() -> new QnAExceptionHandler(ErrorCode.QNA_NOT_FOUND));

        sendSellerNotification(seller.getEmail(), new NotificationRequest("질문 등록 알림", question.getProduct().getName(), event.getQuestionId().toString()));
    }

    // 리뷰 등록 알림
    @Async
    @EventListener
    public void handleReviewPostedEvent(ReviewPostedEvent event) {
        Seller seller = sellerRepository.findById(event.getSellerId())
                .orElseThrow(() -> new UserExceptionHandler(ErrorCode.SELLER_NOT_FOUND));
        Review review = reviewRepository.findById(event.getReviewId())
                .orElseThrow(() -> new ReviewExceptionHandler(ErrorCode.REVIEW_NOT_FOUND));

        sendSellerNotification(seller.getEmail(), new NotificationRequest("리뷰 등록 알림", review.getProduct().getName(), event.getReviewId().toString()));
    }

    // 입금 확인 요청 알림
    @Scheduled(cron = "0 0 10 * * *")
    public void sendCheckPendingPaymentReminders() {
        LocalDate currentDate = LocalDate.now();
        List<Orders> pendingOrders = orderRepository.findByStatusAndCreatedAtBetween(
                OrderStatus.PENDING, currentDate.minusDays(3).atStartOfDay(), currentDate.atStartOfDay());

        for (Orders order : pendingOrders) {
            Seller seller = order.getProduct().getSeller();
            sendSellerNotification(seller.getEmail(), new NotificationRequest("입금 확인 알림", order.getProduct().getName(), String.valueOf(seller.getId())));
        }
    }

    // 오프라인 판매 시 수령 일자 지난 후 수령 확인 요청 알림
    @Scheduled(cron = "0 0 10 * * *")
    public void sendCheckPickupReminㅌders() {
        LocalDate currentDate = LocalDate.now();
        List<Orders> orders = orderRepository.findByStatusAndPickupOptionAndPickupDateBefore(PickupOption.OFFLINE, OrderStatus.PAID, currentDate);

        for (Orders order : orders) {
            Seller seller = order.getProduct().getSeller();
            sendSellerNotification(seller.getEmail(), new NotificationRequest("수령 확인 알림", order.getProduct().getName(), String.valueOf(seller.getId())));
        }
    }

    // 온라인 판매 시 입금 확인 3일 후 배송 요청 알림
    @Scheduled(cron = "0 0 10 * * *")
    public void sendDeliverReminders() {
        LocalDate threeDaysAgo = LocalDate.now().minusDays(3);
        List<Orders> orders = orderRepository.findByStatusAndPickupOptionAndCreatedAt(PickupOption.ONLINE, OrderStatus.PAID, threeDaysAgo);

        for (Orders order : orders) {
            Seller seller = order.getProduct().getSeller();
            sendSellerNotification(seller.getEmail(), new NotificationRequest("배송 요청 알림", order.getProduct().getName(), String.valueOf(seller.getId())));
        }
    }

    // 알림 생성 후 15일 지나면 삭제
    @Scheduled(cron = "0 0 9 * * *")
    public void deleteOldSellerNotifications() {
        LocalDateTime timeLimit = LocalDateTime.now().minusDays(15);
        sellerNotificationRepository.deleteAllByCreatedAtBefore(timeLimit);
    }

    // 알림 전송 메소드
    private void sendSellerNotification(String email, NotificationRequest notificationRequest) {
        try {
            String fcmToken = redisUtil.getFCMToken(email);
            Seller seller = sellerRepository.findByEmail(email)
                    .orElseThrow(() -> new UserExceptionHandler(ErrorCode.SELLER_NOT_FOUND));
            if (fcmToken != null) {
                Message message = Message.builder()
                        .putData("title", notificationRequest.getTitle())
                        .putData("body", notificationRequest.getBody())
                        .putData("id", notificationRequest.getId())
                        .setToken(fcmToken)
                        .build();
                FirebaseMessaging.getInstance().sendAsync(message).get();
                SellerNotification notification = SellerNotification.builder()
                        .seller(seller)
                        .title(notificationRequest.getTitle())
                        .body(notificationRequest.getBody())
                        .build();
                sellerNotificationRepository.save(notification);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // 알림 모아보기
    public SellerNotificationListResponse getSellerNotificationList(Seller seller, Long cursor, int pageSize){
        Pageable pageable = PageRequest.of(0, pageSize);
        List<SellerNotification> notifications;
        if (cursor == null) {
            notifications = sellerNotificationRepository.findBySellerOrderByCreatedAtDesc(seller, pageable);
        } else {
            notifications = sellerNotificationRepository.findBySellerAndIdLessThanOrderByCreatedAtDesc(seller, cursor, pageable);
        }

        Long nextCursor = notifications.isEmpty()?null : notifications.get(notifications.size() - 1).getId();
        Boolean isLast = notifications.size()<pageSize;

        return SellerNotificationListResponse.from(notifications, nextCursor, isLast);

    }
}
