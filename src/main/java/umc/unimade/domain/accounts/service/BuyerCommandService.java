package umc.unimade.domain.accounts.service;

import lombok.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import umc.unimade.domain.accounts.dto.BuyerInfoRequestDto;
import umc.unimade.domain.accounts.dto.ProfileResponseDto;
import umc.unimade.domain.accounts.dto.BuyerUpdateInfoResponseDto;
import umc.unimade.domain.accounts.entity.Buyer;
import umc.unimade.domain.accounts.entity.Seller;
import umc.unimade.domain.accounts.repository.SellerRepository;
import umc.unimade.domain.favorite.entity.FavoriteSeller;
import umc.unimade.domain.favorite.repository.FavoriteSellerRepository;
import umc.unimade.global.common.ApiResponse;
import umc.unimade.global.common.ErrorCode;
import umc.unimade.domain.accounts.exception.UserExceptionHandler;
import umc.unimade.global.util.s3.S3Provider;
import umc.unimade.global.util.s3.dto.S3UploadRequest;

import java.util.Optional;


@Service
@RequiredArgsConstructor
public class BuyerCommandService {

    private final SellerRepository sellerRepository;
    private final FavoriteSellerRepository favoriteSellerRepository;
    private final S3Provider s3Provider;

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

    private Seller findSellerById(Long sellerId) {
        return sellerRepository.findById(sellerId)
                .orElseThrow(() -> new UserExceptionHandler(ErrorCode.SELLER_NOT_FOUND));
    }

    private Optional<FavoriteSeller> findFavoriteSeller(Seller seller,Buyer buyer) {
        return favoriteSellerRepository.findBySellerAndBuyer(seller,buyer);
    }

    @Transactional
    public BuyerUpdateInfoResponseDto buyerInfo(Buyer buyer) {
        return BuyerUpdateInfoResponseDto.of(buyer.getName());
    }

    @Transactional
    public BuyerUpdateInfoResponseDto updateBuyerInfo(Buyer buyer, BuyerInfoRequestDto buyerInfoRequestDto) {
        buyer.updateBuyerInfo(buyerInfoRequestDto.getName());
        return BuyerUpdateInfoResponseDto.of(buyer.getName());
    }

    @Transactional
    public ProfileResponseDto updateBuyerProfile(Buyer buyer, MultipartFile image){
        String imageUrl = s3Provider.uploadFile(image, S3UploadRequest.builder()
                                                                .userId(buyer.getId())
                                                                .dirName("profileBuyer")
                                                                .build());
        buyer.updateBuyerProfile(imageUrl);
        return ProfileResponseDto.of(imageUrl);
    }
}
