package umc.unimade.domain.accounts.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;
import umc.unimade.domain.favorite.entity.FavoriteSeller;
import umc.unimade.domain.products.entity.ProductRegister;
import umc.unimade.domain.products.entity.Products;
import umc.unimade.domain.qna.entity.Answers;
import umc.unimade.domain.review.entity.ReviewAnswer;
import umc.unimade.global.common.BaseEntity;

import java.util.ArrayList;
import java.util.List;

@Entity
@Builder
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Table(name = "seller")
public class Seller extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "seller_id", nullable = false)
    private Long id;

    @Column(name = "name", nullable = false)
    private String name;

    @Column(name = "email", nullable = false, unique = true)
    private String email;

    @Column(name = "password", nullable = false)
    private String password;

    @Column(name = "refresh_token")
    private String refreshToken;

    @Enumerated(EnumType.STRING)
    @Column(name = "gender")
    private Gender gender;

    @Column(name = "phone")
    private String phone;

    @Column(name = "profile_image")
    private String profileImage;

    @Enumerated(EnumType.STRING)
    @Column(name = "role")
    private Role role;

    @Enumerated(EnumType.STRING)
    @Column(name = "provider")
    private Provider provider;

    @Column(name = "is_login")
    private Boolean isLogin;

    @Column(name = "description", columnDefinition = "TEXT")
    private String description; // 설명

    @OneToMany(mappedBy = "seller", fetch = FetchType.LAZY)
    private List<Products> products = new ArrayList<>();

    @OneToMany(mappedBy = "seller", fetch = FetchType.LAZY)
    @JsonIgnore
    private List<ProductRegister> productRegister = new ArrayList<>();

    @OneToMany(mappedBy = "seller", fetch = FetchType.LAZY)
    private List<Answers> answers = new ArrayList<>();

    @OneToMany(mappedBy = "seller", fetch = FetchType.LAZY)
    private List<ReviewAnswer> reviewAnswers = new ArrayList<>();

    @OneToMany(mappedBy = "seller", fetch = FetchType.LAZY)
    private List<FavoriteSeller> favoriteSellers = new ArrayList<>();

    @PostPersist
    private void setRole() {
        role = Role.SELLER;
    }

    @Builder
    public Seller(String name, String email, String password, Gender gender, Role role, String refreshToken) {
        this.name = name;
        this.email = email;
        this.password = password;
        this.refreshToken = refreshToken;
        this.gender = gender;
        this.role = role;
        this.provider = Provider.NORMAL;
        this.isLogin = false;
    }

    public void login(String refreshToken) {
        this.isLogin = true;
        this.refreshToken = refreshToken;
    }

    public void logout() {
        this.isLogin = false;
        this.refreshToken = null;
    }

    public void updateDescription(String description) {
        this.description = description;
    }

    public void updateInfo(String name, String password){
        this.name = name;
        this.password = password;
    }

    public void updateSellerProfile(String imageUrl){
        this.profileImage = imageUrl;
    }
}