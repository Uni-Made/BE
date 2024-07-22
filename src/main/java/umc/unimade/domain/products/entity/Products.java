package umc.unimade.domain.products.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;
import umc.unimade.domain.accounts.entity.Seller;
import umc.unimade.domain.favorite.entity.FavoriteProduct;
import umc.unimade.domain.orders.entity.Orders;
import umc.unimade.domain.orders.entity.PurchaseForm;
import umc.unimade.domain.products.dto.OptionCategoryRequest;
import umc.unimade.domain.products.dto.ProductRequest;
import umc.unimade.domain.qna.entity.Questions;
import umc.unimade.domain.review.entity.Review;
import umc.unimade.global.common.BaseEntity;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Entity
@Builder
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Table(name = "products")
public class Products extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "product_id", nullable = false)
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
    private String accountNumber;

    @Column(name = "account_name", nullable = false)
    private String accountName;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "seller_id")
    @JsonIgnore
    private Seller seller;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "category_id")
    @JsonIgnore
    private Category category;

    @OneToMany(mappedBy = "product", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private List<Orders> orders = new ArrayList<>();

    @OneToMany(mappedBy = "product", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private List<ProductsImage> productImages = new ArrayList<>();

    @OneToMany(mappedBy = "product", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private List<OptionCategory> optionCategories = new ArrayList<>();

    @OneToMany(mappedBy = "product", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private List<Questions> questions = new ArrayList<>();

    @OneToMany(mappedBy = "product", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private List<Review> reviews = new ArrayList<>();

    @OneToMany(mappedBy = "product", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private List<FavoriteProduct> favoriteProducts = new ArrayList<>();

    @OneToMany(mappedBy = "product", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private List<PurchaseForm> PurchaseForms = new ArrayList<>();

    public void updateProduct(ProductRequest.UpdateProductDto request, Category category) {
        this.name = request.getName();
        this.content = request.getContent();
        this.price = request.getPrice();
        this.deadline = request.getDeadline();
        this.status = request.getStatus();
        this.university = request.getUniversity();
        this.pickupOption = request.getPickupOption();
        this.bankName = request.getBankName();
        this.accountNumber = request.getAccountNumber();
        this.accountName = request.getAccountName();
        this.category = category;
    }

    public void updateOptionCategories(List<OptionCategoryRequest> optionRequests) {
        this.optionCategories.clear();

        List<OptionCategory> newOptionCategories = optionRequests.stream()
                .map(optionRequest -> {
                    OptionCategory optionCategory = OptionCategory.builder()
                            .name(optionRequest.getName())
                            .product(this)
                            .build();
                    optionCategory.setValues(optionRequest.getValues().stream()
                            .map(value -> OptionValue.builder()
                                    .value(value)
                                    .category(optionCategory)
                                    .build())
                            .collect(Collectors.toList()));
                    return optionCategory;
                })
                .collect(Collectors.toList());

        this.optionCategories.addAll(newOptionCategories);
    }
}