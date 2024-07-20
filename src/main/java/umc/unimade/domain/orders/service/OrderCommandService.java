package umc.unimade.domain.orders.service;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import umc.unimade.domain.accounts.entity.Buyer;
import umc.unimade.domain.accounts.repository.BuyerRepository;
import umc.unimade.domain.orders.dto.OrderRequest;
import umc.unimade.domain.orders.dto.OrderResponse;
import umc.unimade.domain.orders.entity.*;
import umc.unimade.domain.orders.repository.*;
import umc.unimade.domain.products.entity.OptionValue;
import umc.unimade.domain.products.repository.ProductRepository;
import umc.unimade.domain.products.entity.Products;
import umc.unimade.global.common.ErrorCode;
import umc.unimade.domain.products.exception.ProductsExceptionHandler;
import umc.unimade.domain.accounts.exception.UserExceptionHandler;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class OrderCommandService {

    private final OrderRepository orderRepository;
    private final OrderOptionRepository orderOptionRepository;
    private final BuyerRepository buyerRepository;
    private final PurchaseFormRepository purchaseFormRepository;
    private final ProductRepository productRepository;
    private final OptionValueRepository optionValueRepository;
    private final OrderItemRepository orderItemRepository;

    @Transactional
    public OrderResponse createOrder(Long productId, Long buyerId, OrderRequest orderRequest) {
        Buyer buyer = findBuyerById(buyerId);
        Products product = findProductById(productId);

        // PurchaseForm 엔티티 생성 및 저장
        PurchaseForm purchaseForm = orderRequest.getPurchaseForm().toEntity();
        purchaseFormRepository.save(purchaseForm);

        // Order 생성 및 저장
        Orders order = orderRequest.toOrders(product, buyer, purchaseForm);
        orderRepository.save(order);

        // OrderItem 저장
        List<OrderItem> orderItems = orderRequest.toOrderItems(order, product);
        orderItemRepository.saveAll(orderItems);


        // OptionValue 조회 및 OrderOption 저장
        List<OrderOption> orderOptions = orderItems.stream()
                .flatMap(orderItem -> {
                    OrderRequest.OrderOptionRequest orderOptionRequest = orderRequest.getOrderOptions().get(orderItems.indexOf(orderItem));
                    List<OptionValue> optionValues = optionValueRepository.findAllById(orderOptionRequest.getOptionValueIds());
                    return orderOptionRequest.toOrderOptions(orderItem, optionValues).stream();
                })
                .collect(Collectors.toList());

        orderOptionRepository.saveAll(orderOptions);
        Long totalPrice = orderItems.stream()
                .mapToLong(item -> product.getPrice() * item.getCount())
                .sum();

        return OrderResponse.from(order, product, totalPrice);
    }

    private Buyer findBuyerById(Long buyerId) {
        return buyerRepository.findById(buyerId)
                .orElseThrow(() -> new UserExceptionHandler(ErrorCode.BUYER_NOT_FOUND));
    }
    private Products findProductById(Long productId){
        return productRepository.findById(productId)
                .orElseThrow(() -> new ProductsExceptionHandler(ErrorCode.PRODUCT_NOT_FOUND));
    }

}
