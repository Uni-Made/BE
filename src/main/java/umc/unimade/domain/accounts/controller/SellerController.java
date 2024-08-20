package umc.unimade.domain.accounts.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import umc.unimade.domain.accounts.dto.*;
import umc.unimade.domain.accounts.entity.Seller;
import umc.unimade.domain.accounts.service.SellerCommandService;
import umc.unimade.domain.accounts.service.SellerQueryService;
import org.springframework.data.domain.Pageable;
import umc.unimade.domain.orders.dto.ProductOrderResponse;
import umc.unimade.domain.products.dto.MyPageProductResponse;
import umc.unimade.domain.products.dto.ProductResponse;
import umc.unimade.domain.products.entity.ViewType;
import umc.unimade.domain.products.exception.ProductsExceptionHandler;
import umc.unimade.domain.products.service.ProductsQueryService;
import umc.unimade.domain.qna.dto.QuestionResponse;
import umc.unimade.domain.review.dto.ReviewResponse;
import umc.unimade.global.common.ApiResponse;
import umc.unimade.global.common.ErrorCode;
import umc.unimade.global.security.LoginSeller;

import java.util.List;

@RestController
@RequestMapping("/seller")
@RequiredArgsConstructor
public class SellerController {

    private final SellerQueryService sellerQueryService;
    private final SellerCommandService sellerCommandService;
    private final ProductsQueryService productsQueryService;

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
    @Operation(summary = "판매자 마이페이지 상품 정보 가져오기")
    @GetMapping("/myPage/{productId}")
    public ResponseEntity<ApiResponse<ProductResponse>> getProductDetails (@PathVariable Long productId ,
                                                                           @RequestParam ViewType viewType,
                                                                           @RequestParam(required = false) Long cursor,
                                                                           @RequestParam(defaultValue = "10") int pageSize) {
        try {
            return ResponseEntity.ok(ApiResponse.onSuccess(productsQueryService.getProduct(productId, viewType, cursor, pageSize)));
        }
        catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ApiResponse.onFailure(HttpStatus.BAD_REQUEST.name(), e.getMessage()));
        } catch (ProductsExceptionHandler e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(ApiResponse.onFailure(ErrorCode.PRODUCT_NOT_FOUND.getCode(), ErrorCode.PRODUCT_NOT_FOUND.getMessage()));
        }
    }

    @Tag(name = "Seller", description = "판매자 관련 API")
    @Operation(summary = "판매자 마이페이지 질문 보기(답변완료/미답변)")
    @GetMapping("/myPage/{productId}/qna")
    public ResponseEntity<List<QuestionResponse>> getQuestions(@RequestParam Long productId,
                                                               @RequestParam(required = false, defaultValue = "true") boolean answered) {
        List<QuestionResponse> questions;
        if (answered) {
            questions = sellerQueryService.getAnsweredQuestionsList(productId);
        } else {
            questions = sellerQueryService.getUnansweredQuestionsList(productId);
        }

        return ResponseEntity.ok(questions);
    }

    @Tag(name = "Seller", description = "판매자 관련 API")
    @Operation(summary = "판매자 마이페이지 리뷰 보기(답변완료/미답변)")
    @GetMapping("/myPage/{productId}/review")
    public ResponseEntity<List<ReviewResponse>> getReviews(@RequestParam Long productId,
                                                           @RequestParam(required = false, defaultValue = "true") boolean answered) {
        List<ReviewResponse> reviews;
        if (answered) {
            reviews = sellerQueryService.getAnsweredReviewsList(productId);
        } else {
            reviews = sellerQueryService.getUnansweredReviewsList(productId);
        }

        return ResponseEntity.ok(reviews);
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

    @Tag(name = "Seller", description = "판매자 기본 정보")
    @Operation(summary = "판매자 기본 정보")
    @GetMapping("/info")
    public ApiResponse<SellerInfoResponseDto> sellerInfo(@LoginSeller Seller seller) {
        return ApiResponse.onSuccess(sellerCommandService.sellerInfo(seller));
    }

    @Tag(name = "Seller", description = "판매자 기본 정보")
    @Operation(summary = "판매자 기본 정보")
    @PatchMapping("/update/info")
    public ApiResponse<SellerInfoResponseDto> updateSellerInfo(@LoginSeller Seller seller,
                                                               @RequestBody SellerInfoRequestDto sellerInfoRequestDto) {
        return ApiResponse.onSuccess(sellerCommandService.updateSellerInfo(seller, sellerInfoRequestDto));
    }

    @Tag(name = "Seller", description = "판매자 프로필 수정")
    @Operation(summary = "판매자 프로필 수정")
    @PatchMapping("/update/profile")
    public ApiResponse<ProfileResponseDto> updateSellerProfile(@LoginSeller Seller seller,
                                                               @RequestPart(name = "image", required = false) MultipartFile image) {
        return ApiResponse.onSuccess(sellerCommandService.updateSellerProfile(seller, image));
    }
}
