package umc.unimade.domain.accounts.service;
import lombok.*;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import umc.unimade.domain.accounts.dto.SellerPageResponse;
import umc.unimade.domain.accounts.entity.Seller;
import umc.unimade.domain.accounts.exception.SellerExceptionHandler;
import umc.unimade.domain.accounts.repository.SellerRepository;
import umc.unimade.domain.orders.dto.BuyerOrderHistoryResponse;
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
import umc.unimade.domain.orders.dto.OrderStatusDetailResponse;
import umc.unimade.domain.orders.entity.Orders;
import umc.unimade.domain.orders.exception.OrderExceptionHandler;
import umc.unimade.domain.orders.repository.OrderRepository;
import umc.unimade.domain.products.entity.PickupOption;
import umc.unimade.domain.products.entity.ProductStatus;
import umc.unimade.domain.products.entity.Products;
import umc.unimade.domain.products.repository.ProductRepository;
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
    private final SellerRepository sellerRepository;
    private final ProductRepository productRepository;

    // 구매자 마이페이지
    public BuyerPageResponse getBuyerPage(Buyer buyer){
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

    // 구매 상태 상세 정보
    public OrderStatusDetailResponse getOrderStatusDetail(Long orderId){
        Orders order = findOrderById(orderId);
        switch (order.getStatus()){
            case PENDING :
                return OrderStatusDetailResponse.fromPendingOrder(order);
            case PAID:
                if (order.getPurchaseForm().getPickupOption() == PickupOption.OFFLINE) {
                    return OrderStatusDetailResponse.fromPaidOfflineOrder(order);
                } else if (order.getPurchaseForm().getPickupOption() == PickupOption.ONLINE) {
                    return OrderStatusDetailResponse.fromPaidOnlineOrder(order);
                }
                break;

            default:
                throw new OrderExceptionHandler(ErrorCode.INVALID_ORDER_STATUS);
        }
        throw new OrderExceptionHandler(ErrorCode.INVALID_ORDER_STATUS);
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
    public FavoriteSellersListResponse getFavoriteSellersList(Buyer buyer, Long cursor, int pageSize){
        Pageable pageable = PageRequest.of(0, pageSize, Sort.by(Sort.Direction.DESC, "createdAt"));
        List<FavoriteSeller> favoriteSellers;
        if (cursor == null){
            favoriteSellers = favoriteSellerRepository.findByBuyerOrderByCreatedAtDesc(buyer, pageable);
        }else {
            favoriteSellers = favoriteSellerRepository.findByBuyerAndIdLessThanOrderByCreatedAtDesc(buyer, cursor, pageable);
        }
        Long nextCursor = favoriteSellers.isEmpty() ? null : favoriteSellers.get(favoriteSellers.size() - 1).getId();
        Boolean isLast = favoriteSellers.size() < pageSize;

        return FavoriteSellersListResponse.from(favoriteSellers, nextCursor, isLast);
    }

    private Buyer findBuyerById(Long buyerId) {
        return buyerRepository.findById(buyerId)
                .orElseThrow(() -> new UserExceptionHandler(ErrorCode.BUYER_NOT_FOUND));
    }

    private Orders findOrderById(Long orderId){
        return orderRepository.findById(orderId)
                .orElseThrow(() -> new OrderExceptionHandler(ErrorCode.ORDER_NOT_FOUND));
    }

    // 구매자 시점 메이더 홈 - 특정 sellerId의 프로필 정보와 selling 상태인 상품 목록을 조회
    public SellerPageResponse getSellerPage(Buyer buyer, Long sellerId, String sort, Pageable pageable) {
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
        Long favoriteCount = favoriteSellerRepository.countBySellerId(sellerId);
        boolean favoriteSeller = favoriteSellerRepository.existsBySellerIdAndBuyerId(sellerId, buyer.getId());

        return SellerPageResponse.of(seller, productResponses, favoriteCount, favoriteSeller);
    }
}
