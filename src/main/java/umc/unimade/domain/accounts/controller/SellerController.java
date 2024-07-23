package umc.unimade.domain.accounts.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import umc.unimade.domain.accounts.dto.SellerMyPageResponse;
import umc.unimade.domain.accounts.service.SellerQueryService;


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
}
