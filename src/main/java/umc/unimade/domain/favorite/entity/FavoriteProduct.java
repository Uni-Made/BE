package umc.unimade.domain.favorite.entity;

import jakarta.persistence.*;
import lombok.*;
import umc.unimade.domain.accounts.entity.Buyer;
import umc.unimade.domain.products.entity.Products;
import umc.unimade.global.common.BaseEntity;

@Entity
@Builder
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Table(name = "favorite_product")
public class FavoriteProduct extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "favorite_id", nullable = false)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "buyer_id")
    private Buyer buyer;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "product_id")
    private Products product;
}