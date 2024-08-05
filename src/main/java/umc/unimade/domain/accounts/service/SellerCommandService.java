package umc.unimade.domain.accounts.service;

import org.springframework.transaction.annotation.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import umc.unimade.domain.accounts.entity.Seller;
import umc.unimade.domain.accounts.exception.SellerExceptionHandler;
import umc.unimade.domain.accounts.repository.SellerRepository;
import umc.unimade.global.common.ErrorCode;

@Service
@RequiredArgsConstructor
public class SellerCommandService {

    private final SellerRepository sellerRepository;

    @Transactional
    public void saveDescription(Long sellerId, String description) {
        Seller seller = sellerRepository.findById(sellerId)
                .orElseThrow(() -> new SellerExceptionHandler(ErrorCode.SELLER_NOT_FOUND));
        seller.updateDescription(description);
        sellerRepository.save(seller);
    }
}
