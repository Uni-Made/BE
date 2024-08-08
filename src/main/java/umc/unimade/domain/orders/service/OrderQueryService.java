package umc.unimade.domain.orders.service;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import umc.unimade.domain.accounts.repository.SellerRepository;
import umc.unimade.domain.orders.dto.*;
import umc.unimade.domain.orders.entity.Orders;
import umc.unimade.domain.orders.exception.SellerExceptionhandler;
import umc.unimade.domain.orders.dto.OrderVerificationRequest;
import umc.unimade.domain.orders.dto.OrderVerificationResponse;
import umc.unimade.domain.orders.entity.OrderStatus;
import umc.unimade.domain.orders.exception.OrderExceptionHandler;
import umc.unimade.domain.orders.repository.OptionValueRepository;
import umc.unimade.domain.orders.repository.OrderRepository;
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
    private final OrderRepository orderRepository;
    private final SellerRepository sellerRepository;

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

    public OrderResponse getBankingInfo(Long orderId){
        Orders order = findOrderById(orderId);
        if(order.getStatus() == OrderStatus.PENDING){
            return OrderResponse.from(order,order.getProduct(),order.getTotalPrice());
        }else {
            throw new OrderExceptionHandler(ErrorCode.ORDER_NOT_FOUND);
        }
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

    // 특정 판매자의 구매 요청 확인(조회)
    public Page<SellerOrderResponse> getOrdersBySellerId(Long sellerId, Pageable pageable) {
        sellerRepository.findById(sellerId)
                .orElseThrow(() -> new SellerExceptionhandler(ErrorCode.SELLER_NOT_FOUND));

        Page<Orders> ordersPage = orderRepository.findOrdersBySellerId(sellerId, pageable);

        return ordersPage.map(SellerOrderResponse::from);
    }

    private Orders findOrderById(Long orderId){
        return orderRepository.findById(orderId)
                .orElseThrow(() -> new OrderExceptionHandler(ErrorCode.ORDER_NOT_FOUND));

    }
}
