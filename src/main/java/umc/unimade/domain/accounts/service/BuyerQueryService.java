package umc.unimade.domain.accounts.service;
import lombok.*;
import org.springframework.stereotype.Service;
import umc.unimade.domain.accounts.dto.BuyerPageResponse;
import umc.unimade.domain.accounts.entity.Buyer;
import umc.unimade.domain.accounts.repository.BuyerRepository;
import umc.unimade.domain.favorite.dto.FavoriteProductResponse;
import umc.unimade.domain.favorite.dto.FavoriteSellerResponse;
import umc.unimade.domain.favorite.repository.FavoriteProductRepository;
import umc.unimade.domain.favorite.repository.FavoriteSellerRepository;
import umc.unimade.global.common.ErrorCode;
import umc.unimade.global.common.exception.UserExceptionHandler;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class BuyerQueryService {
    private final BuyerRepository buyerRepository;
    private final FavoriteSellerRepository favoriteSellerRepository;
    private final FavoriteProductRepository favoriteProductRepository;

    public BuyerPageResponse getBuyerPage(Long buyerId){
        Buyer buyer = findBuyerById(buyerId);
        List<FavoriteProductResponse> favoriteProducts = favoriteProductRepository.findTop4ByBuyerOrderByCreatedAtDesc(buyer).stream()
                .map(FavoriteProductResponse::from)
                .collect(Collectors.toList());
        List<FavoriteSellerResponse> favoriteSellers = favoriteSellerRepository.findTop4ByBuyerOrderByCreatedAtDesc(buyer).stream()
                .map(FavoriteSellerResponse::from)
                .collect(Collectors.toList());
        return BuyerPageResponse.from(buyer, favoriteProducts, favoriteSellers);
    }

    private Buyer findBuyerById(Long buyerId) {
        return buyerRepository.findById(buyerId).orElseThrow(() -> new UserExceptionHandler(ErrorCode.BUYER_NOT_FOUND));
    }
}
