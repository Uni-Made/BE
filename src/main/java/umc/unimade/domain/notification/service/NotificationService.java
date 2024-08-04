package umc.unimade.domain.notification.service;


import com.google.firebase.messaging.FirebaseMessaging;
import com.google.firebase.messaging.Message;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import umc.unimade.domain.accounts.entity.Buyer;
import umc.unimade.domain.accounts.entity.Seller;
import umc.unimade.domain.accounts.exception.UserExceptionHandler;
import umc.unimade.domain.accounts.repository.BuyerRepository;
import umc.unimade.domain.accounts.repository.SellerRepository;
import umc.unimade.domain.notification.dto.NotificationRequest;
import umc.unimade.global.common.ErrorCode;
import umc.unimade.global.util.redis.RedisUtil;

@RequiredArgsConstructor
@Service
public class NotificationService {
    private final BuyerRepository buyerRepository;
    private final SellerRepository sellerRepository;
    private final RedisUtil redisUtil;


    @Async
    public void sendBuyerNotification(Long buyerId,NotificationRequest notificationRequest){
        try {
            Buyer buyer = findBuyerById(buyerId);
            String fcmToken = redisUtil.getFCMToken(buyer.getEmail());
            Message message = Message.builder()
                    .putData("title", notificationRequest.getTitle())
                    .putData("body", notificationRequest.getBody())
                    .setToken(fcmToken)
                    .build();

            FirebaseMessaging.getInstance().sendAsync(message).get();
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    @Async
    public void sendSellerNotification(Long sellerId, NotificationRequest notificationRequest){
        try {
            Seller seller = findSellerById(sellerId);
            String fcmToken = redisUtil.getFCMToken(seller.getEmail());
            Message message = Message.builder()
                    .putData("title", notificationRequest.getTitle())
                    .putData("body", notificationRequest.getBody())
                    .setToken(fcmToken)
                    .build();

            FirebaseMessaging.getInstance().sendAsync(message).get();
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    private Buyer findBuyerById(Long buyerId) {
        return buyerRepository.findById(buyerId)
                .orElseThrow(() -> new UserExceptionHandler(ErrorCode.BUYER_NOT_FOUND));
    }

    private Seller findSellerById (Long sellerId) {
        return sellerRepository.findById(sellerId)
                .orElseThrow(() -> new UserExceptionHandler(ErrorCode.SELLER_NOT_FOUND));
    }

}
