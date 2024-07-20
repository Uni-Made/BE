package umc.unimade.domain.products.service.strategy;

import lombok.*;

import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Component;
import umc.unimade.domain.products.dto.ProductResponse;
import umc.unimade.domain.review.repository.ReviewRepository;
import umc.unimade.domain.review.dto.ReviewListResponse;
import umc.unimade.domain.products.entity.Products;


import java.util.List;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Component("reviewStrategy")
public class ReviewStrategy implements ProductStrategy {
    private final ReviewRepository reviewRepository;

    @Override
    public ProductResponse loadProduct(Products product, PageRequest pageRequest) {
        ProductResponse response = ProductResponse.to(product);
        List<ReviewListResponse> reviews = reviewRepository.findByProductId(product.getId(), pageRequest)
                .getContent().stream()
                .map(ReviewListResponse::to)
                .collect(Collectors.toList());
        response.setReviews(reviews);
        return response;
    }
}

