package umc.unimade.domain.accounts.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.*;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import umc.unimade.domain.accounts.dto.*;
import umc.unimade.domain.orders.dto.BuyerOrderHistoryResponse;
import umc.unimade.domain.accounts.entity.Buyer;
import umc.unimade.domain.accounts.service.BuyerCommandService;
import umc.unimade.domain.accounts.service.BuyerQueryService;
import umc.unimade.domain.favorite.dto.FavoriteProductsListResponse;
import umc.unimade.domain.favorite.dto.FavoriteSellersListResponse;
import umc.unimade.domain.orders.dto.OrderStatusDetailResponse;
import umc.unimade.global.common.ApiResponse;
import umc.unimade.global.common.ErrorCode;
import umc.unimade.domain.accounts.exception.UserExceptionHandler;
import umc.unimade.global.security.LoginBuyer;


@RestController
@RequestMapping("/buyer")
@RequiredArgsConstructor
public class BuyerController {
    private final BuyerCommandService buyerCommandService;
    private final BuyerQueryService buyerQueryService;

    @Tag(name = "FavoriteSeller")
    @Operation(summary = "찜하지 않은 상태라면 찜하기. \n 찜한 상태라면 찜하기 취소")
    @PostMapping("/favorite/{sellerId}")
    public ResponseEntity<ApiResponse<Void>> toggleFavoriteSeller(@PathVariable Long sellerId, @LoginBuyer Buyer buyer) {
        try {
            return ResponseEntity.ok(buyerCommandService.toggleFavoriteSeller(sellerId, buyer));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ApiResponse.onFailure(HttpStatus.BAD_REQUEST.name(), e.getMessage()));
        } catch (UserExceptionHandler e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(ApiResponse.onFailure(ErrorCode.BUYER_NOT_FOUND.getCode(), ErrorCode.BUYER_NOT_FOUND.getMessage()));
        }
    }

    @Tag(name = "Buyer", description = "구매자 관련 API , description = {buyerId} 추후 삭제")
    @Operation(summary = "구매자 마이페이지에서 찜한 상품과 메이더를 보여준다.")
    @GetMapping("/myPage")
    public ResponseEntity<ApiResponse<BuyerPageResponse>> getBuyerPage(@LoginBuyer Buyer buyer) {
        try {
            return ResponseEntity.ok(ApiResponse.onSuccess(buyerQueryService.getBuyerPage(buyer)));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ApiResponse.onFailure(HttpStatus.BAD_REQUEST.name(), e.getMessage()));
        } catch (UserExceptionHandler e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(ApiResponse.onFailure(ErrorCode.BUYER_NOT_FOUND.getCode(), ErrorCode.BUYER_NOT_FOUND.getMessage()));
        }
    }
    @Tag(name = "Buyer", description = "구매자 관련 API")
    @Operation(summary = "구매 내역 리스트")
    @GetMapping("/history")
    public ResponseEntity<ApiResponse<BuyerOrderHistoryResponse>> getOrderHistory(
            @LoginBuyer Buyer buyer,
            @RequestParam(required = false) Long cursor,
            @RequestParam int pageSize) {
        try {
            return ResponseEntity.ok(ApiResponse.onSuccess(buyerQueryService.getOrderHistory(buyer, cursor, pageSize)));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ApiResponse.onFailure(HttpStatus.BAD_REQUEST.name(), e.getMessage()));
        } catch (UserExceptionHandler e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(ApiResponse.onFailure(ErrorCode.BUYER_NOT_FOUND.getCode(), ErrorCode.BUYER_NOT_FOUND.getMessage()));
        }
    }

    @Tag(name = "Buyer", description = "구매자 관련 API")
    @Operation(summary = "구매 상세 내역 조회")
    @GetMapping("/history/{orderId}")
    public ResponseEntity<ApiResponse<OrderStatusDetailResponse>> getOrderStatusDetail(@PathVariable Long orderId){
        try {
            return ResponseEntity.ok(ApiResponse.onSuccess(buyerQueryService.getOrderStatusDetail(orderId)));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ApiResponse.onFailure(HttpStatus.BAD_REQUEST.name(), e.getMessage()));
        } catch (UserExceptionHandler e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(ApiResponse.onFailure(ErrorCode.ORDER_NOT_FOUND.getCode(), ErrorCode.ORDER_NOT_FOUND.getMessage()));
        }
    }

    @Tag(name = "FavoriteProduct")
    @Operation(summary = "찜한 상품 더보기")
    @GetMapping("/favorite-products")
    public ResponseEntity<ApiResponse<FavoriteProductsListResponse>> getFavoriteProductsList(@LoginBuyer Buyer buyer,
                                                                                             @RequestParam(required = false) Long cursor,
                                                                                             @RequestParam int pageSize
    ) {
        try {
            return ResponseEntity.ok(ApiResponse.onSuccess(buyerQueryService.getFavoriteProdutsList(buyer, cursor, pageSize)));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ApiResponse.onFailure(HttpStatus.BAD_REQUEST.name(), e.getMessage()));
        } catch (UserExceptionHandler e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(ApiResponse.onFailure(ErrorCode.BUYER_NOT_FOUND.getCode(), ErrorCode.BUYER_NOT_FOUND.getMessage()));
        }
    }

    @Tag(name = "FavoriteSeller")
    @Operation(summary = "찜한 메이더 더보기")
    @GetMapping("/favorite-sellers")
    public ResponseEntity<ApiResponse<FavoriteSellersListResponse>> getFavoriteSellersList(@LoginBuyer Buyer buyer,
                                                                                            @RequestParam(required = false) Long cursor,
                                                                                            @RequestParam int pageSize) {
        try {
            return ResponseEntity.ok(ApiResponse.onSuccess(buyerQueryService.getFavoriteSellersList(buyer, cursor, pageSize)));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ApiResponse.onFailure(HttpStatus.BAD_REQUEST.name(), e.getMessage()));
        } catch (UserExceptionHandler e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(ApiResponse.onFailure(ErrorCode.BUYER_NOT_FOUND.getCode(), ErrorCode.BUYER_NOT_FOUND.getMessage()));
        }
    }

    @Tag(name = "Buyer", description = "구매자 관련 API")
    @Operation(summary = "구매자 시점 메이더 홈", description = "popular/latest/deadline")
    @GetMapping("/{sellerId}")
    public ResponseEntity<SellerPageResponse> getSellerPage(@LoginBuyer Buyer buyer,
                                                            @PathVariable Long sellerId,
                                                            @RequestParam(required = false, defaultValue = "popular") String sort,
                                                            @RequestParam(name = "page", defaultValue = "0") int page,
                                                            @RequestParam(name = "size", defaultValue = "10") int size) {

        Pageable pageable = PageRequest.of(page, size);
        SellerPageResponse sellerPage = buyerQueryService.getSellerPage(buyer, sellerId, sort, pageable);
        return ResponseEntity.ok(sellerPage);
    }

    @Tag(name = "Buyer", description = "구매자 기본 정보")
    @Operation(summary = "구매자 기본 정보")
    @GetMapping("/info")
    public ApiResponse<BuyerUpdateInfoResponseDto> buyerInfo(@LoginBuyer Buyer buyer) {
        return ApiResponse.onSuccess(buyerCommandService.buyerInfo(buyer));
    }

    @Tag(name = "Buyer", description = "구매자 정보수정")
    @Operation(summary = "구매자 정보 수정")
    @PatchMapping("/update/info")
    public ApiResponse<BuyerUpdateInfoResponseDto> updateBuyerInfo(@LoginBuyer Buyer buyer,
                                                                   @RequestBody BuyerInfoRequestDto buyerInfoRequestDto) {
        return ApiResponse.onSuccess(buyerCommandService.updateBuyerInfo(buyer, buyerInfoRequestDto));
    }

    @Tag(name = "Buyer", description = "구매자 프로필 수정")
    @Operation(summary = "구매자 프로필 수정")
    @PatchMapping("/update/profile")
    public ApiResponse<Boolean> updateBuyerProfile(@LoginBuyer Buyer buyer,
                                                   @RequestBody BuyerProfileRequestDto buyerProfileRequestDto) {
        return ApiResponse.onSuccess(buyerCommandService.updateBuyerProfile(buyer, buyerProfileRequestDto));
    }
}
