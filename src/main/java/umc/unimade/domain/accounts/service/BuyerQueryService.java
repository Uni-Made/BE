package umc.unimade.domain.accounts.service;
import lombok.*;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import umc.unimade.domain.accounts.dto.BuyerOrderHistoryResponse;
import umc.unimade.domain.accounts.dto.BuyerPageResponse;
import umc.unimade.domain.accounts.entity.Buyer;
import umc.unimade.domain.accounts.repository.BuyerRepository;
import umc.unimade.domain.favorite.dto.FavoriteProductResponse;
import umc.unimade.domain.favorite.dto.FavoriteProductsListResponse;
import umc.unimade.domain.favorite.dto.FavoriteSellerResponse;
import umc.unimade.domain.favorite.dto.FavoriteSellersListResponse;
import umc.unimade.domain.favorite.entity.FavoriteProduct;
import umc.unimade.domain.favorite.entity.FavoriteSeller;
import umc.unimade.domain.favorite.repository.FavoriteProductRepository;
import umc.unimade.domain.favorite.repository.FavoriteSellerRepository;
import umc.unimade.domain.orders.entity.Orders;
import umc.unimade.domain.orders.repository.OrderRepository;
import umc.unimade.global.common.ErrorCode;
import umc.unimade.domain.accounts.exception.UserExceptionHandler;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class BuyerQueryService {
    private final BuyerRepository buyerRepository;
    private final FavoriteSellerRepository favoriteSellerRepository;
    private final FavoriteProductRepository favoriteProductRepository;
    private final OrderRepository orderRepository;

    // 구매자 마이페이지
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

    // 구매 내역
    public BuyerOrderHistoryResponse getOrderHistory(Buyer buyer, Long cursor, Integer pageSize){
        Pageable pageable = PageRequest.of(0, pageSize);
        List<Orders> orders = orderRepository.findOrdersByBuyerWithCursorPagination(buyer, cursor, pageable);

        Long nextCursor = orders.isEmpty() ? null : orders.get(orders.size() - 1).getId();
        boolean isLast = orders.size() < pageSize;
        return BuyerOrderHistoryResponse.from(orders, nextCursor, isLast);
    }

    // 찜한 상품 더보기
    public FavoriteProductsListResponse getFavoriteProdutsList(Buyer currentBuyer,Long cursor, int pageSize){
        Buyer buyer = findBuyerById(currentBuyer.getId());

        Pageable pageable = PageRequest.of(0, pageSize, Sort.by(Sort.Direction.DESC, "createdAt"));

        List<FavoriteProduct> favoriteProducts;

        if (cursor == null) {
            favoriteProducts = favoriteProductRepository.findByBuyerOrderByCreatedAtDesc(buyer, pageable);
        } else {
            favoriteProducts = favoriteProductRepository.findByBuyerAndIdLessThanOrderByCreatedAtDesc(buyer, cursor, pageable);
        }

        Long nextCursor = favoriteProducts.isEmpty() ? null : favoriteProducts.get(favoriteProducts.size() - 1).getId();
        Boolean isLast = favoriteProducts.size() < pageSize;

        return FavoriteProductsListResponse.from(favoriteProducts, nextCursor, isLast);
    }

    // 찜한 메이더 더보기
    public FavoriteSellersListResponse getFavoriteSellersList(Long buyerId, Long cursor, int pageSize){
        Pageable pageable = PageRequest.of(0, pageSize, Sort.by(Sort.Direction.DESC, "createdAt"));
        List<FavoriteSeller> favoriteSellers;
        if (cursor == null){
            favoriteSellers = favoriteSellerRepository.findByBuyerIdOrderByCreatedAtDesc(buyerId, pageable);
        }else {
            favoriteSellers = favoriteSellerRepository.findByBuyerIdAndIdLessThanOrderByCreatedAtDesc(buyerId, cursor, pageable);
        }
        Long nextCursor = favoriteSellers.isEmpty() ? null : favoriteSellers.get(favoriteSellers.size() - 1).getId();
        Boolean isLast = favoriteSellers.size() < pageSize;

        return FavoriteSellersListResponse.from(favoriteSellers, nextCursor, isLast);
    }

    private Buyer findBuyerById(Long buyerId) {
        return buyerRepository.findById(buyerId)
                .orElseThrow(() -> new UserExceptionHandler(ErrorCode.BUYER_NOT_FOUND));
    }
}
