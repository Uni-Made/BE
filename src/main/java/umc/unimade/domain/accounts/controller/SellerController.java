package umc.unimade.domain.accounts.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import umc.unimade.domain.accounts.dto.SellerMyPageResponse;
import umc.unimade.domain.accounts.dto.SellerPageResponse;
import umc.unimade.domain.accounts.service.SellerQueryService;
import org.springframework.data.domain.Pageable;
import umc.unimade.domain.products.dto.MyPageProductResponse;

@RestController
@RequestMapping("/seller")
@RequiredArgsConstructor
public class SellerController {

    private final SellerQueryService sellerQueryService;

    @Tag(name = "Seller", description = "판매자 관련 API")
    @Operation(summary = "판매자 마이페이지")
    @GetMapping("/myPage/{sellerId}")
    public ResponseEntity<SellerMyPageResponse> getSellerMyPage(@PathVariable Long sellerId) {
        SellerMyPageResponse response = sellerQueryService.getSellerMyPage(sellerId);
        return ResponseEntity.ok(response);
    }

    @Tag(name = "Seller", description = "판매자 관련 API")
    @Operation(summary = "판매자 마이페이지 판매 중 상품 더보기")
    @GetMapping("/myPage/sellingProducts/{sellerId}")
    public ResponseEntity<Page<MyPageProductResponse>> getSellingProductsList(@PathVariable Long sellerId,
                                                                              @RequestParam(name = "page", defaultValue = "0") int page,
                                                                              @RequestParam(name = "size", defaultValue = "10") int size) {
        Pageable pageable = PageRequest.of(page, size); // TODO - 커서로 변경
        Page<MyPageProductResponse> response = sellerQueryService.getSellingProductsList(sellerId, pageable);
        return ResponseEntity.ok(response);
    }

    @Tag(name = "Seller", description = "판매자 관련 API")
    @Operation(summary = "판매자 마이페이지 판매 종료 상품 더보기")
    @GetMapping("/myPage/soldoutProducts/{sellerId}")
    public ResponseEntity<Page<MyPageProductResponse>> getSoldoutProductsList(@PathVariable Long sellerId,
                                                                              @RequestParam(name = "page", defaultValue = "0") int page,
                                                                              @RequestParam(name = "size", defaultValue = "10") int size) {
        Pageable pageable = PageRequest.of(page, size); // TODO - 커서로 변경
        Page<MyPageProductResponse> response = sellerQueryService.getSoldoutProductsList(sellerId, pageable);
        return ResponseEntity.ok(response);
    }

    @Tag(name = "Seller", description = "판매자 관련 API")
    @Operation(summary = "특정 판매자 페이지")
    @GetMapping("/{sellerId}")
    public ResponseEntity<SellerPageResponse> getSellerPage(@PathVariable Long sellerId,
                                                                      @RequestParam(required = false, defaultValue = "popular") String sort,
                                                                      @RequestParam(name = "page", defaultValue = "0") int page,
                                                                      @RequestParam(name = "size", defaultValue = "10") int size) {

        Pageable pageable = PageRequest.of(page, size); // TODO - 커서로 변경
        SellerPageResponse sellerPage = sellerQueryService.getSellerPage(sellerId, sort, pageable);
        return ResponseEntity.ok(sellerPage);
    }
}
