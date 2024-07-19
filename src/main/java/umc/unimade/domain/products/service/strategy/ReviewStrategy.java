package umc.unimade.domain.products.service.strategy;

import lombok.*;

import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Component;
import umc.unimade.domain.products.dto.ProductResponse;
import umc.unimade.domain.review.ReviewRepository;
import umc.unimade.domain.review.dto.ReviewResponse;
import umc.unimade.domain.products.entity.Products;
import umc.unimade.domain.products.service.strategy.ProductStrategy;


import java.util.List;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Component("reviewStrategy")
public class ReviewStrategy implements ProductStrategy {
    private final ReviewRepository reviewRepository;

    @Override
    public ProductResponse loadProduct(Products product, PageRequest pageRequest) {
        ProductResponse response = ProductResponse.to(product);
        List<ReviewResponse> reviews = reviewRepository.findByProductId(product.getId(), pageRequest)
                .getContent().stream()
                .map(ReviewResponse::to)
                .toList();
        response.setReviews(reviews.stream()
                .map(ReviewResponse::getContent) // assuming you want to extract content here
                .collect(Collectors.toList()));
        return response;
    }
}
