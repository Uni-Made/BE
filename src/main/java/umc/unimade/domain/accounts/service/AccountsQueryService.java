package umc.unimade.domain.accounts.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import umc.unimade.domain.accounts.entity.Buyer;
import umc.unimade.domain.accounts.entity.Seller;
import umc.unimade.domain.accounts.exception.BuyerExceptionHandler;
import umc.unimade.domain.accounts.exception.SellerExceptionHandler;
import umc.unimade.domain.accounts.repository.BuyerRepository;
import umc.unimade.domain.accounts.repository.SellerRepository;
import umc.unimade.global.common.ErrorCode;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class AccountsQueryService {

    private final BuyerRepository buyerRepository;
    private final SellerRepository sellerRepository;


    public Buyer getBuyerByEmail(String email) {
        return buyerRepository.findByEmail(email)
                .orElseThrow(() -> new BuyerExceptionHandler(ErrorCode.BUYER_NOT_FOUND));
    }

    public Seller getSellerByEmail(String email) {
        return sellerRepository.findByEmail(email)
                .orElseThrow(() -> new SellerExceptionHandler(ErrorCode.SELLER_NOT_FOUND));
    }
}