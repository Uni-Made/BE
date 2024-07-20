package umc.unimade.domain.review.service;

import org.springframework.stereotype.Service;
import lombok.*;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import umc.unimade.domain.accounts.entity.Buyer;
import umc.unimade.domain.accounts.repository.BuyerRepository;
import umc.unimade.domain.products.repository.ProductRepository;
import umc.unimade.domain.products.entity.Products;
import umc.unimade.domain.review.repository.ReviewRepository;
import umc.unimade.domain.review.dto.ReviewCreateRequest;
import umc.unimade.domain.review.entity.Review;
import umc.unimade.domain.review.entity.ReviewImage;
import umc.unimade.global.common.ErrorCode;
import umc.unimade.domain.products.exception.ProductsExceptionHandler;
import umc.unimade.domain.accounts.exception.UserExceptionHandler;
import umc.unimade.global.util.s3.S3Provider;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ReviewCommandService {
    private final ReviewRepository reviewRepository;
    private final ProductRepository productRepository;
    private final BuyerRepository buyerRepository;
    private final S3Provider s3Provider;

    @Transactional
    public void createReview(Long productId, Long buyerId, ReviewCreateRequest reviewCreateRequest, List<MultipartFile> images) {
        Products product = findProductById(productId);
        Buyer buyer = findBuyerById(buyerId);
        Review review = reviewCreateRequest.toEntity(product, buyer);
        List<ReviewImage> reviewImages = reviewCreateRequest.toReviewImages(images, s3Provider, buyerId, review);
        if (reviewImages != null) {
            review.setReviewImages(reviewImages);
        }
        reviewRepository.save(review);
    }
    private Buyer findBuyerById(Long buyerId) {
        return buyerRepository.findById(buyerId).orElseThrow(() -> new UserExceptionHandler(ErrorCode.BUYER_NOT_FOUND));
    }
    private Products findProductById(Long productId){
        return productRepository.findById(productId).orElseThrow(() -> new ProductsExceptionHandler(ErrorCode.PRODUCT_NOT_FOUND));
    }
}
