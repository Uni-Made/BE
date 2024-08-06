package umc.unimade.domain.review.service;

import org.springframework.stereotype.Service;
import lombok.*;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import umc.unimade.domain.accounts.entity.Buyer;
import umc.unimade.domain.accounts.entity.Seller;
import umc.unimade.domain.accounts.exception.UserExceptionHandler;
import umc.unimade.domain.orders.entity.Orders;
import umc.unimade.domain.orders.entity.Orders;
import umc.unimade.domain.orders.exception.OrderExceptionHandler;
import umc.unimade.domain.orders.repository.OrderRepository;
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
import umc.unimade.global.util.s3.S3Provider;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ReviewCommandService {
    private final ReviewRepository reviewRepository;
    private final OrderRepository orderRepository;
    private final S3Provider s3Provider;
    private final ReviewReportRepository reviewReportRepository;

    @Transactional
    public void createReview(Long orderId, Buyer buyer, ReviewCreateRequest reviewCreateRequest, List<MultipartFile> images) {
        if (reviewCreateRequest.getRatingStar() == 0) {
            throw new ReviewExceptionHandler(ErrorCode.INVALID_RATING_STAR);
        }
        Orders order = findOrderById(orderId);
        if (!order.getBuyer().getId().equals(buyer.getId())) {
            throw new UserExceptionHandler(ErrorCode.REVIEW_CREATE_NOT_BUYER);
        }
        Review review = reviewCreateRequest.toEntity(order);
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

    private Review findReviewById(Long reviewId){
        return reviewRepository.findById(reviewId)
                .orElseThrow(() -> new ReviewExceptionHandler(ErrorCode.REVIEW_NOT_FOUND));
    }

    private Orders findOrderById(Long orderId){
        return orderRepository.findById(orderId)
                .orElseThrow(() -> new OrderExceptionHandler(ErrorCode.ORDER_NOT_FOUND));
    }

    @Transactional
    public ReviewReportResponse reportReview(Long reviewId, ReviewReportRequest request, Seller seller) {

        Review review = reviewRepository.findById(reviewId)
                .orElseThrow(() -> new ReviewExceptionHandler(ErrorCode.REVIEW_NOT_FOUND));

        // 자신의 상품에 대한 리뷰가 아닌 경우
        if (!review.getProduct().getSeller().getId().equals(seller.getId())) {
            throw new ReviewExceptionHandler(ErrorCode.REVIEW_NOT_YOURS);
        }

        ReviewReport report = request.toEntity(review, seller);
        reviewReportRepository.save(report);

        return ReviewReportResponse.from(report);
    }
}