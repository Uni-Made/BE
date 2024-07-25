package umc.unimade.domain.accounts.service;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import umc.unimade.domain.accounts.dto.SellerMyPageResponse;
import umc.unimade.domain.accounts.dto.SellerPageResponse;
import umc.unimade.domain.accounts.entity.Seller;
import umc.unimade.domain.accounts.exception.SellerExceptionHandler;
import umc.unimade.domain.accounts.repository.SellerRepository;
import umc.unimade.domain.products.dto.SellingProductResponse;
import umc.unimade.domain.products.dto.SoldoutProductResponse;
import umc.unimade.domain.products.entity.ProductStatus;
import umc.unimade.domain.products.entity.Products;
import umc.unimade.domain.products.repository.ProductRepository;
import umc.unimade.global.common.ErrorCode;
import org.springframework.data.domain.Pageable;

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

    // 특정 sellerId의 프로필 정보와 selling 상태인 상품 목록을 조회
    public SellerPageResponse getSellerPage(Long sellerId, String sort, Pageable pageable) {
        Seller seller = sellerRepository.findById(sellerId)
                .orElseThrow(() -> new SellerExceptionHandler(ErrorCode.SELLER_NOT_FOUND));

        // 정렬
        Page<Products> products;
        switch (sort) {
            case "popular": // 인기순
                products = productRepository.findBySellerIdAndStatusOrderByPopularity(sellerId, ProductStatus.SELLING, pageable);
                break;
            case "latest": // 최신순
                products = productRepository.findBySellerIdAndStatusOrderByCreatedAtDesc(sellerId, ProductStatus.SELLING, pageable);
                break;
            case "deadline": // 마감순
                products = productRepository.findBySellerIdAndStatusOrderByDeadline(sellerId, ProductStatus.SELLING, pageable);
                break;
            default:
                products = productRepository.findBySellerIdAndStatusOrderByPopularity(sellerId, ProductStatus.SELLING, pageable);
        }

        Page<SellerPageResponse.ProductsResponse> productResponses = products.map(SellerPageResponse.ProductsResponse::from);

        return SellerPageResponse.of(seller, productResponses);
    }
}