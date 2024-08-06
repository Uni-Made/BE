package umc.unimade.domain.accounts.service;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import umc.unimade.domain.accounts.dto.SellerMyPageResponse;
import umc.unimade.domain.accounts.entity.Seller;
import umc.unimade.domain.accounts.repository.SellerRepository;
import umc.unimade.domain.favorite.repository.FavoriteSellerRepository;
import umc.unimade.domain.products.dto.MyPageProductResponse;
import umc.unimade.domain.products.entity.ProductStatus;
import umc.unimade.domain.products.repository.ProductRepository;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class SellerQueryService {

    private final SellerRepository sellerRepository;
    private final ProductRepository productRepository;
    private final FavoriteSellerRepository favoriteSellerRepository;

    // 판매자 마이페이지
    public SellerMyPageResponse getSellerMyPage(Seller seller) {
        List<MyPageProductResponse> sellingProducts = productRepository.findTop4BySellerIdAndStatusOrderByCreatedAtDesc(seller.getId(), ProductStatus.SELLING)
                .stream()
                .map(MyPageProductResponse::from)
                .collect(Collectors.toList());

        List<MyPageProductResponse> soldoutProducts = productRepository.findTop4BySellerIdAndStatusOrderByCreatedAtDesc(seller.getId(), ProductStatus.SOLDOUT)
                .stream()
                .map(MyPageProductResponse::from)
                .collect(Collectors.toList());

        Long favoriteCount = favoriteSellerRepository.countBySellerId(seller.getId());

        return SellerMyPageResponse.from(seller, sellingProducts, soldoutProducts, favoriteCount);
    }

    // 판매자 마이페이지 - selling 상태인 상품 목록 더보기
    public Page<MyPageProductResponse> getSellingProductsList(Seller seller, Pageable pageable) {
        return productRepository.findBySellerIdAndStatusOrderByCreatedAtDesc(seller.getId(), ProductStatus.SELLING, pageable)
                .map(MyPageProductResponse::from);
    }

    // 판매자 마이페이지 - soldout 상태인 상품 목록 더보기
    public Page<MyPageProductResponse> getSoldoutProductsList(Seller seller, Pageable pageable) {
        return productRepository.findBySellerIdAndStatusOrderByCreatedAtDesc(seller.getId(), ProductStatus.SOLDOUT, pageable)
                .map(MyPageProductResponse::from);
    }

}