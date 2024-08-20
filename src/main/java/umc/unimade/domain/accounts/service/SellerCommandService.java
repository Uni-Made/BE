package umc.unimade.domain.accounts.service;

import org.springframework.transaction.annotation.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import umc.unimade.domain.accounts.dto.*;
import umc.unimade.domain.accounts.entity.Seller;
import umc.unimade.domain.accounts.exception.SellerExceptionHandler;
import umc.unimade.domain.accounts.repository.SellerRepository;
import umc.unimade.global.common.ErrorCode;
import umc.unimade.global.util.s3.S3Provider;
import umc.unimade.global.util.s3.dto.S3UploadRequest;

@Service
@RequiredArgsConstructor
public class SellerCommandService {

    private final SellerRepository sellerRepository;
    private final S3Provider s3Provider;

    @Transactional
    public void saveDescription(Seller seller, String description) {
        seller.updateDescription(description);
        sellerRepository.save(seller);
    }

    @Transactional
    public SellerInfoResponseDto sellerInfo(Seller seller) {
        return SellerInfoResponseDto.of(seller);
    }

    @Transactional
    public SellerInfoResponseDto updateSellerInfo(Seller seller, SellerInfoRequestDto sellerInfoRequestDto) {
        if(sellerRepository.existsByPassword(sellerInfoRequestDto.getPassword())){
            seller.updateInfo(sellerInfoRequestDto.getName(), sellerInfoRequestDto.getNewPassword());
            sellerRepository.save(seller);
            return SellerInfoResponseDto.of(seller);
        }
        throw new SellerExceptionHandler(ErrorCode.SELLER_INVALID_PASSWORD);
    }

    @Transactional
    public ProfileResponseDto updateSellerProfile(Seller seller, MultipartFile image){
        String imageUrl = s3Provider.uploadFile(image, S3UploadRequest.builder()
                .userId(seller.getId())
                .dirName("profileSeller")
                .build());
        seller.updateSellerProfile(imageUrl);
        return ProfileResponseDto.of(imageUrl);
    }
}
