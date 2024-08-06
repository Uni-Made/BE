package umc.unimade.domain.accounts.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import umc.unimade.domain.accounts.dto.SellerMyPageResponse;
import umc.unimade.domain.accounts.service.SellerCommandService;
import umc.unimade.domain.accounts.service.SellerQueryService;
import org.springframework.data.domain.Pageable;
import umc.unimade.domain.products.dto.MyPageProductResponse;

@RestController
@RequestMapping("/seller")
@RequiredArgsConstructor
public class SellerController {

    private final SellerQueryService sellerQueryService;
    private final SellerCommandService sellerCommandService;

    @Tag(name = "Seller", description = "판매자 관련 API")
    @Operation(summary = "판매자 마이페이지, sellerId 제거 예정")
    @GetMapping("/myPage/{sellerId}")
    public ResponseEntity<SellerMyPageResponse> getSellerMyPage(@PathVariable Long sellerId) {
        SellerMyPageResponse response = sellerQueryService.getSellerMyPage(sellerId);
        return ResponseEntity.ok(response);
    }

    @Tag(name = "Seller", description = "판매자 관련 API")
    @Operation(summary = "판매자 마이페이지에서 설명창 입력 받기, sellerId 제거 예정")
    @PutMapping("/{sellerId}/description")
    public ResponseEntity<Void> updateDescription(@PathVariable Long sellerId, @RequestBody String description) {
        sellerCommandService.saveDescription(sellerId, description);
        return ResponseEntity.ok().build();
    }

    @Tag(name = "Seller", description = "판매자 관련 API")
    @Operation(summary = "판매자 마이페이지 판매 중 상품 더보기, sellerId 제거 예정")
    @GetMapping("/myPage/sellingProducts/{sellerId}")
    public ResponseEntity<Page<MyPageProductResponse>> getSellingProductsList(@PathVariable Long sellerId,
                                                                              @RequestParam(name = "page", defaultValue = "0") int page,
                                                                              @RequestParam(name = "size", defaultValue = "12") int size) {
        Pageable pageable = PageRequest.of(page, size);
        Page<MyPageProductResponse> response = sellerQueryService.getSellingProductsList(sellerId, pageable);
        return ResponseEntity.ok(response);
    }

    @Tag(name = "Seller", description = "판매자 관련 API")
    @Operation(summary = "판매자 마이페이지 판매 종료 상품 더보기, sellerId 제거 예정")
    @GetMapping("/myPage/soldoutProducts/{sellerId}")
    public ResponseEntity<Page<MyPageProductResponse>> getSoldoutProductsList(@PathVariable Long sellerId,
                                                                              @RequestParam(name = "page", defaultValue = "0") int page,
                                                                              @RequestParam(name = "size", defaultValue = "12") int size) {
        Pageable pageable = PageRequest.of(page, size);
        Page<MyPageProductResponse> response = sellerQueryService.getSoldoutProductsList(sellerId, pageable);
        return ResponseEntity.ok(response);
    }
}
