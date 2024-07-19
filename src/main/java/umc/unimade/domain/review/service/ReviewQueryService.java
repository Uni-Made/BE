package umc.unimade.domain.review.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import umc.unimade.domain.review.ReviewRepository;
import umc.unimade.domain.review.dto.ReviewResponse;
import umc.unimade.global.common.ErrorCode;
import umc.unimade.global.common.exception.ProductsExceptionHandler;
import umc.unimade.global.common.exception.ReviewExceptionHandler;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class ReviewQueryService {

    private final ReviewRepository reviewRepository;

    @Transactional
    public ReviewResponse getReview(Long reviewId){
        return reviewRepository.findById(reviewId)
                .map(ReviewResponse::to)
                .orElseThrow(() -> new ReviewExceptionHandler(ErrorCode.REVIEW_NOT_FOUND));
    }
}
