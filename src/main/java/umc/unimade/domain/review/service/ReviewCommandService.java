package umc.unimade.domain.review.service;

import org.springframework.stereotype.Service;
import lombok.*;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import umc.unimade.domain.accounts.entity.Buyer;
import umc.unimade.domain.accounts.entity.Seller;
import umc.unimade.domain.accounts.exception.SellerExceptionHandler;
import umc.unimade.domain.accounts.repository.SellerRepository;
import umc.unimade.domain.products.repository.ProductRepository;
import umc.unimade.domain.products.entity.Products;
import umc.unimade.domain.review.dto.ReviewReportRequest;
import umc.unimade.domain.review.dto.ReviewReportResponse;
import umc.unimade.domain.review.entity.ReviewReport;
import umc.unimade.domain.review.exception.ReviewExceptionHandler;
import umc.unimade.domain.review.repository.ReviewReportRepository;
import umc.unimade.domain.review.repository.ReviewRepository;
import umc.unimade.domain.review.dto.ReviewCreateRequest;
import umc.unimade.domain.review.entity.Review;
import umc.unimade.domain.review.entity.ReviewImage;
import umc.unimade.global.common.ErrorCode;
import umc.unimade.domain.products.exception.ProductsExceptionHandler;
import umc.unimade.global.util.s3.S3Provider;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ReviewCommandService {
    private final ReviewRepository reviewRepository;
    private final ProductRepository productRepository;
    private final S3Provider s3Provider;
    private final SellerRepository sellerRepository;
    private final ReviewReportRepository reviewReportRepository;

    @Transactional
    public void createReview(Long productId, Buyer buyer, ReviewCreateRequest reviewCreateRequest, List<MultipartFile> images) {
        if (reviewCreateRequest.getRatingStar() == 0 || reviewCreateRequest.getRatingStar()==null) {
            throw new ReviewExceptionHandler(ErrorCode.INVALID_RATING_STAR);
        }
        Products product = findProductById(productId);
        Review review = reviewCreateRequest.toEntity(product, buyer);
        List<ReviewImage> reviewImages = reviewCreateRequest.toReviewImages(images, s3Provider, buyer.getId(), review);
        if (reviewImages != null) {
            review.setReviewImages(reviewImages);
        }
        reviewRepository.save(review);
    }

    @Transactional
    public void deleteReview(Long reviewId,Buyer buyer) {
        Review review = findReviewById(reviewId);
        if (!review.getBuyer().getId().equals(buyer.getId())) {
            throw new ReviewExceptionHandler(ErrorCode.REVIEW_DELETE_NOT_OWNER);
        }
        reviewRepository.delete(review);
    }

    private Products findProductById(Long productId){
        return productRepository.findById(productId)
                .orElseThrow(() -> new ProductsExceptionHandler(ErrorCode.PRODUCT_NOT_FOUND));
    }

    private Review findReviewById(Long reviewId){
        return reviewRepository.findById(reviewId)
                .orElseThrow(() -> new ReviewExceptionHandler(ErrorCode.REVIEW_NOT_FOUND));
    }

    @Transactional
    public ReviewReportResponse reportReview(Long reviewId, ReviewReportRequest request) {

        Review review = reviewRepository.findById(reviewId)
                .orElseThrow(() -> new ReviewExceptionHandler(ErrorCode.REVIEW_NOT_FOUND));

        // TODO: 컨트롤러에서 seller 받아오기
        Seller seller = sellerRepository.findById(1L)
                .orElseThrow(() -> new SellerExceptionHandler(ErrorCode.SELLER_NOT_FOUND));


        ReviewReport report = request.toEntity(review, seller);
        reviewReportRepository.save(report);

        return ReviewReportResponse.from(report);
    }
}
