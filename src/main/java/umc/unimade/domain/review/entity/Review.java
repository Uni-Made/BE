package umc.unimade.domain.review.entity;

import jakarta.persistence.*;
import lombok.*;
import umc.unimade.domain.accounts.entity.Buyer;
import umc.unimade.domain.products.entity.Products;
import umc.unimade.global.common.BaseEntity;

import java.util.ArrayList;
import java.util.List;

@Entity
@Builder
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Table(name = "review")
public class Review extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "review_id", nullable = false)
    private Long id;

    @Column(name = "title", nullable = false)
    private String title;

    @Column(name = "content", nullable = false)
    private String content;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "product_id")
    private Products product;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "buyer_id")
    private Buyer buyer;

    @OneToMany(mappedBy = "review", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private List<ReviewImage> reviewImages = new ArrayList<>();
}