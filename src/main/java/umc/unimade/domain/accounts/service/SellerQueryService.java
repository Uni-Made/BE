package umc.unimade.domain.accounts.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import umc.unimade.domain.accounts.dto.SellerMyPageResponse;
import umc.unimade.domain.accounts.entity.Seller;
import umc.unimade.domain.accounts.exception.SellerExceptionHandler;
import umc.unimade.domain.accounts.repository.SellerRepository;
import umc.unimade.domain.products.dto.SellingProductResponse;
import umc.unimade.domain.products.dto.SoldoutProductResponse;
import umc.unimade.domain.products.entity.ProductStatus;
import umc.unimade.domain.products.repository.ProductRepository;
import umc.unimade.global.common.ErrorCode;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class SellerQueryService {

    private final SellerRepository sellerRepository;
    private final ProductRepository productRepository;

    @Transactional(readOnly = true)
    public SellerMyPageResponse getSellerMyPage(Long sellerId) {
        Seller seller = sellerRepository.findById(sellerId)
                .orElseThrow(() -> new SellerExceptionHandler(ErrorCode.SELLER_NOT_FOUND));

        List<SellingProductResponse> sellingProducts = productRepository.findTop4BySellerIdAndStatusOrderByCreatedAtDesc(sellerId, ProductStatus.SELLING)
                .stream()
                .map(SellingProductResponse::from)
                .collect(Collectors.toList());

        List<SoldoutProductResponse> soldoutProducts = productRepository.findTop4BySellerIdAndStatusOrderByCreatedAtDesc(sellerId, ProductStatus.SOLDOUT)
                .stream()
                .map(SoldoutProductResponse::from)
                .collect(Collectors.toList());

        return SellerMyPageResponse.from(seller, sellingProducts, soldoutProducts);
    }
}
