package umc.unimade.domain.accounts.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.*;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import umc.unimade.domain.accounts.dto.BuyerOrderHistoryResponse;
import umc.unimade.domain.accounts.dto.BuyerPageResponse;
import umc.unimade.domain.accounts.entity.Buyer;
import umc.unimade.domain.accounts.repository.BuyerRepository;
import umc.unimade.domain.accounts.service.BuyerCommandService;
import umc.unimade.domain.accounts.service.BuyerQueryService;
import umc.unimade.domain.favorite.dto.FavoriteProductsListResponse;
import umc.unimade.domain.favorite.dto.FavoriteSellersListResponse;
import umc.unimade.global.common.ApiResponse;
import umc.unimade.global.common.ErrorCode;
import umc.unimade.domain.accounts.exception.UserExceptionHandler;


@RestController
@RequestMapping("/api/buyer")
@RequiredArgsConstructor
public class BuyerController {
    private final BuyerCommandService buyerCommandService;
    private final BuyerQueryService buyerQueryService;
    //임시로 추가
    private final BuyerRepository buyerRepository;

    @Tag(name = "FavoriteSeller")
    @Operation(summary = "찜하지 않은 상태라면 찜하기. \n 찜한 상태라면 찜하기 취소 , description = {buyerId} 추후 삭제 ")
    @PostMapping("/favorite/{sellerId}/{buyerId}")
    public ResponseEntity<ApiResponse<Void>> toggleFavoriteSeller( @PathVariable Long sellerId, @PathVariable Long buyerId) {
        try {
            Buyer currentBuyer = findBuyerById(buyerId);
            return ResponseEntity.ok(buyerCommandService.toggleFavoriteSeller(sellerId, currentBuyer));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ApiResponse.onFailure(HttpStatus.BAD_REQUEST.name(), e.getMessage()));
        } catch (UserExceptionHandler e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(ApiResponse.onFailure(ErrorCode.BUYER_NOT_FOUND.getCode(), ErrorCode.BUYER_NOT_FOUND.getMessage()));
        }
    }

    @Tag(name = "Buyer", description = "구매자 관련 API , description = {buyerId} 추후 삭제")
    @Operation(summary = "구매자 마이페이지에서 찜한 상품과 메이더를 보여준다.")
    @GetMapping("/myPage/{buyerId}")
    public ResponseEntity<ApiResponse<BuyerPageResponse>> getBuyerPage(@PathVariable Long buyerId) {
        try {
            Buyer currentBuyer = findBuyerById(buyerId);
            return ResponseEntity.ok(ApiResponse.onSuccess(buyerQueryService.getBuyerPage(currentBuyer)));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ApiResponse.onFailure(HttpStatus.BAD_REQUEST.name(), e.getMessage()));
        } catch (UserExceptionHandler e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(ApiResponse.onFailure(ErrorCode.BUYER_NOT_FOUND.getCode(), ErrorCode.BUYER_NOT_FOUND.getMessage()));
        }
    }
    @Tag(name = "Buyer", description = "구매자 관련 API")
    @Operation(summary = "구매 내역 리스트 , description = {buyerId} 추후 삭제")
    @GetMapping("/history/{buyerId}")
    public ResponseEntity<ApiResponse<BuyerOrderHistoryResponse>> getOrderHistory(
            @PathVariable Long buyerId,
            @RequestParam(required = false) Long cursor,
            @RequestParam int pageSize) {
        try {
            Buyer currentBuyer = findBuyerById(buyerId);
            return ResponseEntity.ok(ApiResponse.onSuccess(buyerQueryService.getOrderHistory(currentBuyer, cursor, pageSize)));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ApiResponse.onFailure(HttpStatus.BAD_REQUEST.name(), e.getMessage()));
        } catch (UserExceptionHandler e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(ApiResponse.onFailure(ErrorCode.BUYER_NOT_FOUND.getCode(), ErrorCode.BUYER_NOT_FOUND.getMessage()));
        }
    }

    @Tag(name = "FavoriteProduct")
    @Operation(summary = "찜한 상품 더보기")
    @GetMapping("/favorite-products")
    public ResponseEntity<ApiResponse<FavoriteProductsListResponse>> getFavoriteProductsList(@AuthenticationPrincipal Buyer currentBuyer,
                                                                                             @RequestParam(required = false) Long cursor,
                                                                                             @RequestParam int pageSize
    ) {
        try {
            return ResponseEntity.ok(ApiResponse.onSuccess(buyerQueryService.getFavoriteProdutsList(currentBuyer, cursor, pageSize)));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ApiResponse.onFailure(HttpStatus.BAD_REQUEST.name(), e.getMessage()));
        } catch (UserExceptionHandler e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(ApiResponse.onFailure(ErrorCode.BUYER_NOT_FOUND.getCode(), ErrorCode.BUYER_NOT_FOUND.getMessage()));
        }
    }

    @Tag(name = "FavoriteSeller")
    @Operation(summary = "찜한 메이더 더보기")
    @GetMapping("/favorite-sellers")
    public ResponseEntity<ApiResponse<FavoriteSellersListResponse>> getFavoriteSellersList(@AuthenticationPrincipal Buyer currentBuyer,
                                                                                            @RequestParam(required = false) Long cursor,
                                                                                            @RequestParam int pageSize) {
        try {
            return ResponseEntity.ok(ApiResponse.onSuccess(buyerQueryService.getFavoriteSellersList(currentBuyer, cursor, pageSize)));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ApiResponse.onFailure(HttpStatus.BAD_REQUEST.name(), e.getMessage()));
        } catch (UserExceptionHandler e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(ApiResponse.onFailure(ErrorCode.BUYER_NOT_FOUND.getCode(), ErrorCode.BUYER_NOT_FOUND.getMessage()));
        }
    }

    // 임시로 추가
    private Buyer findBuyerById(Long buyerId) {
        return buyerRepository.findById(buyerId)
                .orElseThrow(() -> new UserExceptionHandler(ErrorCode.BUYER_NOT_FOUND));
    }
}
