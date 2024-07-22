package umc.unimade.domain.accounts.service;
import lombok.*;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import umc.unimade.domain.accounts.dto.BuyerOrderHistoryResponse;
import umc.unimade.domain.accounts.dto.BuyerPageResponse;
import umc.unimade.domain.accounts.entity.Buyer;
import umc.unimade.domain.accounts.repository.BuyerRepository;
import umc.unimade.domain.favorite.dto.FavoriteProductResponse;
import umc.unimade.domain.favorite.dto.FavoriteSellerResponse;
import umc.unimade.domain.favorite.repository.FavoriteProductRepository;
import umc.unimade.domain.favorite.repository.FavoriteSellerRepository;
import umc.unimade.domain.orders.entity.Orders;
import umc.unimade.domain.orders.repository.OrderRepository;
import umc.unimade.global.common.CursorPageRequest;
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

    public BuyerOrderHistoryResponse getOrderHistory(Long buyerId, Long cursor, Integer pageSize){
        Pageable pageable = PageRequest.of(0, pageSize);
        List<Orders> orders = orderRepository.findOrdersByBuyerIdWithCursorPagination(buyerId, cursor, pageable);

        Long nextCursor = orders.isEmpty() ? null : orders.get(orders.size() - 1).getId();
        boolean isLast = orders.size() < pageSize;
        return BuyerOrderHistoryResponse.from(orders, nextCursor, isLast);
    }

    private Buyer findBuyerById(Long buyerId) {
        return buyerRepository.findById(buyerId)
                .orElseThrow(() -> new UserExceptionHandler(ErrorCode.BUYER_NOT_FOUND));
    }
}
