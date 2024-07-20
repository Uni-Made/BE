package umc.unimade.domain.orders.service;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import umc.unimade.domain.accounts.entity.Buyer;
import umc.unimade.domain.accounts.repository.BuyerRepository;
import umc.unimade.domain.orders.dto.OrderRequest;
import umc.unimade.domain.orders.dto.OrderResponse;
import umc.unimade.domain.orders.entity.OrderItem;
import umc.unimade.domain.orders.entity.OrderStatus;
import umc.unimade.domain.orders.entity.Orders;
import umc.unimade.domain.orders.entity.PurchaseForm;
import umc.unimade.domain.orders.repository.OptionValueRepository;
import umc.unimade.domain.orders.repository.OrderItemRepository;
import umc.unimade.domain.orders.repository.OrderRepository;
import umc.unimade.domain.orders.repository.PurchaseFormRepository;
import umc.unimade.domain.products.repository.ProductRepository;
import umc.unimade.domain.products.entity.Products;
import umc.unimade.global.common.ErrorCode;
import umc.unimade.global.common.exception.ProductsExceptionHandler;
import umc.unimade.global.common.exception.UserExceptionHandler;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class OrderCommandService {

    private final OrderRepository orderRepository;
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

        // 주문 엔티티 생성
        Orders order = Orders.builder()
                .status(OrderStatus.PENDING) // 초기 상태 설정
                .purchaseForm(purchaseForm)
                .build();

        orderRepository.save(order);

        List<OrderItem> orderItems = orderRequest.getOrderOptions().stream()
                .map(orderOptionRequest -> orderOptionRequest.toEntity(order, product))
                .collect(Collectors.toList());

        orderItemRepository.saveAll(orderItems);

        Long totalPrice = calculateTotalPrice(orderItems);
        return OrderResponse.fromOrder(product, order, totalPrice);
    }

    private Buyer findBuyerById(Long buyerId) {
        return buyerRepository.findById(buyerId).orElseThrow(() -> new UserExceptionHandler(ErrorCode.BUYER_NOT_FOUND));
    }
    private Products findProductById(Long productId){
        return productRepository.findById(productId).orElseThrow(() -> new ProductsExceptionHandler(ErrorCode.PRODUCT_NOT_FOUND));
    }

    private Long calculateTotalPrice(List<OrderItem> orderItems) {
        return orderItems.stream()
                .mapToLong(orderItem -> orderItem.getProduct().getPrice() * orderItem.getCount())
                .sum();
    }
}
