package umc.unimade.domain.accounts.service;

import org.springframework.transaction.annotation.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import umc.unimade.domain.accounts.dto.BuyerInfoRequestDto;
import umc.unimade.domain.accounts.dto.BuyerUpdateInfoResponseDto;
import umc.unimade.domain.accounts.dto.SellerInfoRequestDto;
import umc.unimade.domain.accounts.dto.SellerInfoResponseDto;
import umc.unimade.domain.accounts.entity.Buyer;
import umc.unimade.domain.accounts.entity.Seller;
import umc.unimade.domain.accounts.repository.SellerRepository;

@Service
@RequiredArgsConstructor
public class SellerCommandService {

    private final SellerRepository sellerRepository;

    @Transactional
    public void saveDescription(Seller seller, String description) {
        seller.updateDescription(description);
        sellerRepository.save(seller);
    }

    @Transactional
    public SellerInfoResponseDto sellerInfo(Seller seller) {
        return SellerInfoResponseDto.of(seller);
    }

//    @Transactional
//    public SellerInfoResponseDto updateBuyerInfo(Seller seller, SellerInfoRequestDto sellerInfoRequestDto) {
//
//        buyer.updateBuyerInfo(buyerInfoRequestDto.getName());
//        return BuyerUpdateInfoResponseDto.of(buyer.getName());
//    }
}
