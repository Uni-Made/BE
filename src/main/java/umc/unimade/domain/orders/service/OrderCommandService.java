package umc.unimade.domain.orders.service;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import umc.unimade.domain.accounts.entity.Buyer;
import umc.unimade.domain.orders.dto.OrderRequest;
import umc.unimade.domain.orders.dto.OrderResponse;
import umc.unimade.domain.orders.entity.*;
import umc.unimade.domain.orders.exception.OrderExceptionHandler;
import umc.unimade.domain.orders.repository.*;
import umc.unimade.domain.products.entity.OptionValue;
import umc.unimade.domain.products.repository.ProductRepository;
import umc.unimade.domain.products.entity.Products;
import umc.unimade.global.common.ErrorCode;
import umc.unimade.domain.products.exception.ProductsExceptionHandler;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class OrderCommandService {

    private final OrderRepository orderRepository;
    private final OrderOptionRepository orderOptionRepository;
    private final PurchaseFormRepository purchaseFormRepository;
    private final ProductRepository productRepository;
    private final OptionValueRepository optionValueRepository;
    private final OrderItemRepository orderItemRepository;

    @Transactional
    public OrderResponse createOrder(Long productId, Buyer buyer, OrderRequest orderRequest) {
        if (!orderRequest.getPurchaseForm().getIsAgree()) {
            throw new OrderExceptionHandler(ErrorCode.ORDER_AGREEMENT_REQUIRED);
        }
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

        // 총 가격 계산 및 Orders 엔티티 업데이트
        Long totalPrice = orderItems.stream()
                .mapToLong(item -> product.getPrice() * item.getCount())
                .sum();
        order.setTotalPrice(totalPrice);
        orderRepository.save(order);

        return OrderResponse.from(order, product, totalPrice);
    }

    private Products findProductById(Long productId){
        return productRepository.findById(productId)
                .orElseThrow(() -> new ProductsExceptionHandler(ErrorCode.PRODUCT_NOT_FOUND));
    }

    // 주문 상태 변경 PENDING,PAID,RECEIVED
    @Transactional
    public Orders changeOrderStatus(Long orderId, OrderStatus status) {
        Orders order = orderRepository.findById(orderId)
                .orElseThrow(() -> new OrderExceptionHandler(ErrorCode.ORDER_NOT_FOUND));
        order.setStatus(status);
        return orderRepository.save(order);
    }

    // 수령 상태 변경 NOT_RECEIVED, RECEIVED
    @Transactional
    public Orders changeReceiveStatus(Long orderId) {
        Orders order = orderRepository.findById(orderId)
                .orElseThrow(() -> new OrderExceptionHandler(ErrorCode.ORDER_NOT_FOUND));

        if (order.getStatus() == OrderStatus.PENDING) {
            throw new OrderExceptionHandler(ErrorCode.STATUS_IS_PENDING);
        }

        order.setStatus(OrderStatus.RECEIVED);
        order.setReceiveStatus(ReceiveStatus.RECEIVED);
        return orderRepository.save(order);
    }

    // 구매폼 작성 후 3일 이후 미입금 시 구매 취소
    @Scheduled(cron = "0 0 0 * * *") // 0시 0분 0초
    @Transactional
    public void cancelOldOrders() {
        LocalDateTime threeDaysAgo = LocalDateTime.now().minusDays(3); // 지금으로부터 3일 전 날짜 찾기
        List<Orders> Orders = orderRepository.findOrdersCreatedBefore(threeDaysAgo, OrderStatus.PENDING); // 3일 전
        for (Orders order : Orders) {
            order.setStatus(OrderStatus.CANCELLED);
            orderRepository.save(order);
        }
    }
}
