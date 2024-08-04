package umc.unimade.domain.notification.service;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import umc.unimade.domain.accounts.entity.Buyer;
import umc.unimade.domain.accounts.entity.Seller;
import umc.unimade.domain.accounts.exception.UserExceptionHandler;
import umc.unimade.domain.accounts.repository.BuyerRepository;
import umc.unimade.domain.accounts.repository.SellerRepository;
import umc.unimade.domain.notification.entity.BuyerNotification;
import umc.unimade.domain.notification.entity.SellerNotification;
import umc.unimade.domain.notification.repository.BuyerNotificationRepository;
import umc.unimade.domain.notification.repository.SellerNotificationRepository;
import umc.unimade.global.common.ErrorCode;

@Service
@RequiredArgsConstructor
public class NotificationService {
    private final BuyerNotificationRepository buyerNotificationRepository;
    private final SellerNotificationRepository sellerNotificationRepository;
    private final BuyerRepository buyerRepository;
    private final SellerRepository sellerRepository;

    @Transactional
    public void saveBuyerNotification(Long buyerId, String token){
        Buyer buyer = findBuyerById(buyerId);
        BuyerNotification buyerNotification = BuyerNotification.builder()
                .buyer(buyer)
                .token(token)
                .build();
        buyerNotificationRepository.save(buyerNotification);
    }

    @Transactional
    public void saveSellerNotification(Long sellerId, String token) {
        Seller seller = findSellerById(sellerId);
        SellerNotification sellerNotification = SellerNotification.builder()
                .seller(seller)
                .token(token)
                .build();
        sellerNotificationRepository.save(sellerNotification);
    }
    private Buyer findBuyerById(Long buyerId) {
        return buyerRepository.findById(buyerId)
                .orElseThrow(() -> new UserExceptionHandler(ErrorCode.BUYER_NOT_FOUND));
    }

    private Seller findSellerById(Long sellerId){
        return sellerRepository.findById(sellerId)
                .orElseThrow(() -> new UserExceptionHandler(ErrorCode.SELLER_NOT_FOUND));
    }
}
