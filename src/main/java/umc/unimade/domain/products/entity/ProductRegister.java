package umc.unimade.domain.products.entity;

import jakarta.persistence.*;
import lombok.*;
import umc.unimade.domain.accounts.entity.Seller;
import umc.unimade.global.registerStatus.RegisterStatus;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Entity
@Builder
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Table(name = "product_register")
public class ProductRegister {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "product_register_id", nullable = false)
    private Long id;

    @Column(name = "name", nullable = false)
    private String name;

    @Column(name = "content", nullable = false)
    private String content;

    @Column(name = "price", nullable = false)
    private Long price;

    @Column(name = "deadline")
    private LocalDate deadline;

    @Enumerated(EnumType.STRING)
    @Column(name = "status")
    private ProductStatus status;

    @Column(name = "university", nullable = false)
    private String university;

    @Enumerated(EnumType.STRING)
    @Column(name = "pickup_option", nullable = false)
    private PickupOption pickupOption;

    @Column(name = "bank_name", nullable = false)
    private String bankName;

    @Column(name = "account_number", nullable = false)
    private Long accountNumber;

    @Column(name = "account_name", nullable = false)
    private String accountName;

    @Enumerated(EnumType.STRING)
    @Column(name = "registerStatus", nullable = false)
    private RegisterStatus registerStatus;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "seller_id")
    private Seller seller;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "category_id")
    private Category category;

    @OneToMany(mappedBy = "productRegister", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private List<ProductsImage> productImages = new ArrayList<>();

    @PostPersist
    private void setRegisterStatus() {
        registerStatus = RegisterStatus.PENDING;
    }
}