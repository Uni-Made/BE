package umc.unimade.domain.products.service.strategy;

import lombok.*;

import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;
import umc.unimade.domain.products.dto.ProductResponse;
import umc.unimade.domain.review.entity.Review;
import umc.unimade.domain.review.repository.ReviewRepository;
import umc.unimade.domain.review.dto.ReviewListResponse;
import umc.unimade.domain.products.entity.Products;
import java.util.List;

@RequiredArgsConstructor
@Component("reviewStrategy")
public class ReviewStrategy implements ProductStrategy {
    private final ReviewRepository reviewRepository;

    @Override
    public ProductResponse loadProduct(Products product, Long cursor, int pageSize) {
        Pageable pageable = PageRequest.of(0, pageSize);
        List<Review> reviews = reviewRepository.findByProductIdWithCursorPagination(product.getId(), cursor,pageable);
        Long nextCursor = reviews.isEmpty() ? null : reviews.get(reviews.size() - 1).getId();
        Boolean isLast = reviews.size() < pageSize;

        ReviewListResponse reviewListResponse = ReviewListResponse.from(reviews, nextCursor, isLast);
        return ProductResponse.toReview(product, reviewListResponse);
    }
}

