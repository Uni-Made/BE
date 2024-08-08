package umc.unimade.domain.accounts.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import umc.unimade.domain.accounts.dto.SellerMyPageResponse;
import umc.unimade.domain.accounts.entity.Seller;
import umc.unimade.domain.accounts.service.SellerCommandService;
import umc.unimade.domain.accounts.service.SellerQueryService;
import org.springframework.data.domain.Pageable;
import umc.unimade.domain.orders.dto.ProductOrderResponse;
import umc.unimade.domain.orders.service.OrderQueryService;
import umc.unimade.domain.products.dto.MyPageProductResponse;
import umc.unimade.global.security.LoginSeller;

@RestController
@RequestMapping("/seller")
@RequiredArgsConstructor
public class SellerController {

    private final SellerQueryService sellerQueryService;
    private final SellerCommandService sellerCommandService;

    @Tag(name = "Seller", description = "판매자 관련 API")
    @Operation(summary = "판매자 마이페이지")
    @GetMapping("/myPage")
    public ResponseEntity<SellerMyPageResponse> getSellerMyPage(@LoginSeller Seller seller) {
        SellerMyPageResponse response = sellerQueryService.getSellerMyPage(seller);
        return ResponseEntity.ok(response);
    }

    @Tag(name = "Seller", description = "판매자 관련 API")
    @Operation(summary = "판매자 마이페이지에서 설명창 입력 받기")
    @PutMapping("/description")
    public ResponseEntity<SellerMyPageResponse> updateDescription(@LoginSeller Seller seller,
                                                  @RequestBody String description) {
        sellerCommandService.saveDescription(seller, description);
        SellerMyPageResponse response = sellerQueryService.getSellerMyPage(seller);
        return ResponseEntity.ok(response);
    }

    @Tag(name = "Seller", description = "판매자 관련 API")
    @Operation(summary = "판매자 마이페이지 판매 중 상품 더보기")
    @GetMapping("/myPage/sellingProducts")
    public ResponseEntity<Page<MyPageProductResponse>> getSellingProductsList(@LoginSeller Seller seller,
                                                                              @RequestParam(name = "page", defaultValue = "0") int page,
                                                                              @RequestParam(name = "size", defaultValue = "12") int size) {
        Pageable pageable = PageRequest.of(page, size);
        Page<MyPageProductResponse> response = sellerQueryService.getSellingProductsList(seller, pageable);
        return ResponseEntity.ok(response);
    }

    @Tag(name = "Seller", description = "판매자 관련 API")
    @Operation(summary = "판매자 마이페이지 판매 종료 상품 더보기")
    @GetMapping("/myPage/soldoutProducts")
    public ResponseEntity<Page<MyPageProductResponse>> getSoldoutProductsList(@LoginSeller Seller seller,
                                                                              @RequestParam(name = "page", defaultValue = "0") int page,
                                                                              @RequestParam(name = "size", defaultValue = "12") int size) {
        Pageable pageable = PageRequest.of(page, size);
        Page<MyPageProductResponse> response = sellerQueryService.getSoldoutProductsList(seller, pageable);
        return ResponseEntity.ok(response);
    }

    @Tag(name = "Seller", description = "판매자 관련 API")
    @Operation(summary = "특정 상품의 구매 요청 보기")
    @GetMapping("/product/{productId}")
    public ResponseEntity<Page<ProductOrderResponse>> getOrdersByProductId(@PathVariable Long productId,
                                                                           @RequestParam(name = "page", defaultValue = "0") int page,
                                                                           @RequestParam(name = "size", defaultValue = "10") int size) {
        Pageable pageable = PageRequest.of(page, size);
        Page<ProductOrderResponse> orders = sellerQueryService.getOrdersByProductId(productId, pageable);
        return ResponseEntity.ok(orders);
    }
}
