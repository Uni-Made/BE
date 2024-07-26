package umc.unimade.domain.accounts.service;

import lombok.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import umc.unimade.domain.accounts.entity.Buyer;
import umc.unimade.domain.accounts.entity.Seller;
import umc.unimade.domain.accounts.repository.BuyerRepository;
import umc.unimade.domain.accounts.repository.SellerRepository;
import umc.unimade.domain.favorite.entity.FavoriteSeller;
import umc.unimade.domain.favorite.repository.FavoriteSellerRepository;
import umc.unimade.global.common.ApiResponse;
import umc.unimade.global.common.ErrorCode;
import umc.unimade.domain.accounts.exception.UserExceptionHandler;

import java.util.Optional;

import static umc.unimade.domain.accounts.entity.QBuyer.buyer;

@Service
@RequiredArgsConstructor
public class BuyerCommandService {

    private final BuyerRepository buyerRepository;
    private final SellerRepository sellerRepository;
    private final FavoriteSellerRepository favoriteSellerRepository;

    @Transactional
    public ApiResponse<Void> toggleFavoriteSeller(Long sellerId, Buyer buyer){
        Seller seller = findSellerById(sellerId);
        Optional<FavoriteSeller> existingFavorite = findFavoriteSeller(seller,buyer);

        if(existingFavorite.isPresent()){
            favoriteSellerRepository.delete(existingFavorite.get());
            return ApiResponse.CANCELED_LIKE();
        }else{
            FavoriteSeller favoriteSeller = FavoriteSeller.builder()
                    .buyer(buyer)
                    .seller(seller)
                    .build();
            favoriteSellerRepository.save(favoriteSeller);
            return ApiResponse.SUCCESS_LIKE();
        }

    }

    private Buyer findBuyerById(Long buyerId) {
        return buyerRepository.findById(buyerId)
                .orElseThrow(() -> new UserExceptionHandler(ErrorCode.BUYER_NOT_FOUND));
    }

    private Seller findSellerById(Long sellerId) {
        return sellerRepository.findById(sellerId)
                .orElseThrow(() -> new UserExceptionHandler(ErrorCode.SELLER_NOT_FOUND));
    }

    private Optional<FavoriteSeller> findFavoriteSeller(Seller seller,Buyer buyer) {
        return favoriteSellerRepository.findBySellerAndBuyer(seller,buyer);
    }
}
