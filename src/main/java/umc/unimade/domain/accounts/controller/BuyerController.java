package umc.unimade.domain.accounts.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.*;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import umc.unimade.domain.accounts.service.BuyerCommandService;
import umc.unimade.global.common.ApiResponse;
import umc.unimade.global.common.ErrorCode;
import umc.unimade.global.common.exception.UserExceptionHandler;

@RestController
@RequestMapping("/api/buyer")
@RequiredArgsConstructor
public class BuyerController {
    private final BuyerCommandService buyerCommandService;

    //To do : buyerId 추후에 토큰으로 변경
    //To do : 더 적절한 위치의 디렉토리 고려
    @Tag(name = "favoriteSeller", description = "메이더 찜하기/취소 API")
    @Operation(summary = "찜하지 않은 상태라면 찜하기. \n 찜한 상태라면 찜하기 취소")
    @PostMapping("/favorite/{sellerId}/{buyerId}")
    public ResponseEntity<ApiResponse<Void>> toggleFavoriteSeller(@PathVariable Long sellerId, @PathVariable Long buyerId) {
        try{
            return ResponseEntity.ok(buyerCommandService.toggleFavoriteSeller(sellerId,buyerId));
        }catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ApiResponse.onFailure(HttpStatus.BAD_REQUEST.name(), e.getMessage()));
        } catch (UserExceptionHandler e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(ApiResponse.onFailure(ErrorCode.BUYER_NOT_FOUND.getCode(), ErrorCode.BUYER_NOT_FOUND.getMessage()));
        }
    }
}
