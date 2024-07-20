package umc.unimade.domain.orders.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import umc.unimade.domain.orders.dto.OrderVerificationRequest;
import umc.unimade.domain.orders.dto.OrderVerificationResponse;
import umc.unimade.domain.orders.repository.OptionValueRepository;
import umc.unimade.domain.products.entity.OptionValue;
import umc.unimade.domain.products.entity.Products;
import umc.unimade.domain.products.exception.ProductsExceptionHandler;
import umc.unimade.domain.products.repository.ProductRepository;
import umc.unimade.global.common.ErrorCode;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class OrderQueryService {

    private final OptionValueRepository optionValueRepository;
    private final ProductRepository productRepository;

    public OrderVerificationResponse verifyOrder(Long productId, OrderVerificationRequest request){
        Products product = findProductById(productId);

        List<OrderVerificationResponse.OrderOptionResponse> orderOptionResponses = request.getOrderOptions().stream()
                .map(optionRequest -> {
                    List<OptionValue> optionValues = optionValueRepository.findAllById(optionRequest.getOptionValueIds());
                    return OrderVerificationResponse.OrderOptionResponse.of(optionValues, optionRequest.getCount());
                })
                .collect(Collectors.toList());

        Long totalPrice = calculateTotalPrice(product, request.getOrderOptions());

        return OrderVerificationResponse.from(orderOptionResponses, totalPrice);
    }

    private Products findProductById(Long productId){
        return productRepository.findById(productId)
                .orElseThrow(() -> new ProductsExceptionHandler(ErrorCode.PRODUCT_NOT_FOUND));
    }
    private Long calculateTotalPrice(Products product, List<OrderVerificationRequest.OrderOptionRequest> orderOptions) {
        return orderOptions.stream()
                .mapToLong(option -> product.getPrice() * option.getCount())
                .sum();
    }
}
