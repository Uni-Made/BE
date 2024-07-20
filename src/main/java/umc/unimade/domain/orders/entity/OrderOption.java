package umc.unimade.domain.orders.entity;

import jakarta.persistence.*;
import lombok.*;
import umc.unimade.domain.products.entity.OptionValue;
import umc.unimade.global.common.BaseEntity;

@Entity
@Builder
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Table(name = "order_option")
public class OrderOption extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "order_option_id", nullable = false)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "order_item_id", nullable = false)
    private OrderItem orderItem;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "option_value_id", nullable = false)
    private OptionValue optionValue;

    public void setOrderItem(OrderItem orderItem) {
        this.orderItem = orderItem;
    }
}