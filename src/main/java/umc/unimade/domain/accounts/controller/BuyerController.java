package umc.unimade.domain.accounts.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.*;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import umc.unimade.domain.accounts.dto.BuyerOrderHistoryResponse;
import umc.unimade.domain.accounts.dto.BuyerPageResponse;
import umc.unimade.domain.accounts.service.BuyerCommandService;
import umc.unimade.domain.accounts.service.BuyerQueryService;
import umc.unimade.domain.favorite.dto.FavoriteProductsListResponse;
import umc.unimade.domain.favorite.dto.FavoriteSellersListResponse;
import umc.unimade.global.common.ApiResponse;
import umc.unimade.global.common.CursorPageRequest;
import umc.unimade.global.common.ErrorCode;
import umc.unimade.domain.accounts.exception.UserExceptionHandler;


@RestController
@RequestMapping("/api/buyer")
@RequiredArgsConstructor
public class BuyerController {
    private final BuyerCommandService buyerCommandService;
    private final BuyerQueryService buyerQueryService;

    //To do : buyerId 추후에 토큰으로 변경
    //To do : 더 적절한 위치의 디렉토리 고려

    @Tag(name = "FavoriteSeller")
    @Operation(summary = "찜하지 않은 상태라면 찜하기. \n 찜한 상태라면 찜하기 취소")
    @PostMapping("/favorite/{sellerId}/{buyerId}")
    public ResponseEntity<ApiResponse<Void>> toggleFavoriteSeller(@PathVariable Long sellerId, @PathVariable Long buyerId) {
        try {
            return ResponseEntity.ok(buyerCommandService.toggleFavoriteSeller(sellerId, buyerId));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ApiResponse.onFailure(HttpStatus.BAD_REQUEST.name(), e.getMessage()));
        } catch (UserExceptionHandler e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(ApiResponse.onFailure(ErrorCode.BUYER_NOT_FOUND.getCode(), ErrorCode.BUYER_NOT_FOUND.getMessage()));
        }
    }

    @Tag(name = "Buyer", description = "구매자 관련 API")
    @Operation(summary = "구매자 마이페이지에서 찜한 상품과 메이더를 보여준다.")
    @GetMapping("/myPage/{buyerId}")
    public ResponseEntity<ApiResponse<BuyerPageResponse>> getBuyerPage(@PathVariable Long buyerId) {
        try {
            return ResponseEntity.ok(ApiResponse.onSuccess(buyerQueryService.getBuyerPage(buyerId)));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ApiResponse.onFailure(HttpStatus.BAD_REQUEST.name(), e.getMessage()));
        } catch (UserExceptionHandler e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(ApiResponse.onFailure(ErrorCode.BUYER_NOT_FOUND.getCode(), ErrorCode.BUYER_NOT_FOUND.getMessage()));
        }
    }
    @Tag(name = "Buyer", description = "구매자 관련 API")
    @Operation(summary = "구매 내역 리스트")
    @GetMapping("/history/{buyerId}")
    public ResponseEntity<ApiResponse<BuyerOrderHistoryResponse>> getOrderHistory(
            @RequestParam Long buyerId,
            @RequestParam(required = false) Long cursor,
            @RequestParam int pageSize) {
        try {
            return ResponseEntity.ok(ApiResponse.onSuccess(buyerQueryService.getOrderHistory(buyerId, cursor, pageSize)));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ApiResponse.onFailure(HttpStatus.BAD_REQUEST.name(), e.getMessage()));
        } catch (UserExceptionHandler e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(ApiResponse.onFailure(ErrorCode.BUYER_NOT_FOUND.getCode(), ErrorCode.BUYER_NOT_FOUND.getMessage()));
        }
    }

    @Tag(name = "FavoriteProduct")
    @Operation(summary = "찜한 상품 더보기")
    @GetMapping("/{buyerId}/favorite-products")
    public ResponseEntity<ApiResponse<FavoriteProductsListResponse>> getFavoriteProductsList(@PathVariable Long buyerId,
                                                                                             @RequestParam(required = false) Long cursor,
                                                                                             @RequestParam int pageSize) {
        try {
            return ResponseEntity.ok(ApiResponse.onSuccess(buyerQueryService.getFavoriteProdutsList(buyerId, cursor, pageSize)));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ApiResponse.onFailure(HttpStatus.BAD_REQUEST.name(), e.getMessage()));
        } catch (UserExceptionHandler e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(ApiResponse.onFailure(ErrorCode.BUYER_NOT_FOUND.getCode(), ErrorCode.BUYER_NOT_FOUND.getMessage()));
        }
    }

    @Tag(name = "FavoriteSeller")
    @Operation(summary = "찜한 메이더 더보기")
    @GetMapping("/{buyerId}/favorite-sellers")
    public ResponseEntity<ApiResponse<FavoriteSellersListResponse>> getFavoriteSellersList(@PathVariable Long buyerId,
                                                                                            @RequestParam(required = false) Long cursor,
                                                                                            @RequestParam int pageSize) {
        try {
            return ResponseEntity.ok(ApiResponse.onSuccess(buyerQueryService.getFavoriteSellersList(buyerId, cursor, pageSize)));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ApiResponse.onFailure(HttpStatus.BAD_REQUEST.name(), e.getMessage()));
        } catch (UserExceptionHandler e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(ApiResponse.onFailure(ErrorCode.BUYER_NOT_FOUND.getCode(), ErrorCode.BUYER_NOT_FOUND.getMessage()));
        }
    }
}
