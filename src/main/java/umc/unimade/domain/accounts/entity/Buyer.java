package umc.unimade.domain.accounts.entity;

import jakarta.persistence.*;
import lombok.*;
import umc.unimade.domain.favorite.entity.FavoriteProduct;
import umc.unimade.domain.favorite.entity.FavoriteSeller;
import umc.unimade.domain.notification.entity.BuyerNotification;
import umc.unimade.domain.orders.entity.Orders;
import umc.unimade.domain.qna.entity.Questions;
import umc.unimade.domain.review.entity.ReportStatus;
import umc.unimade.domain.review.entity.Review;
import umc.unimade.global.common.BaseEntity;

import java.util.ArrayList;
import java.util.List;

@Entity
@Builder
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Table(name = "buyer")
public class Buyer extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "buyer_id", nullable = false)
    private Long id;

    @Column(name = "name", nullable = false)
    private String name;

    @Column(name = "email", nullable = false, unique = true)
    private String email;

    @Column(name = "refresh_token", columnDefinition = "TEXT")
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

    @Column(name = "social_id")
    private String socialId;

    @Column(name = "status")
    private ReportStatus status; // 정지 상태

    @OneToMany(mappedBy = "buyer", fetch = FetchType.LAZY)
    private List<Review> reviews = new ArrayList<>();

    @OneToMany(mappedBy = "buyer", fetch = FetchType.LAZY)
    private List<Questions> questions = new ArrayList<>();

    @OneToMany(mappedBy = "buyer", fetch = FetchType.LAZY)
    private List<FavoriteProduct> favoriteProducts = new ArrayList<>();

    @OneToMany(mappedBy = "buyer", fetch = FetchType.LAZY)
    private List<FavoriteSeller> favoriteSellers = new ArrayList<>();

    @OneToMany(mappedBy = "buyer", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private List<Orders> orders = new ArrayList<>();

    @OneToMany(mappedBy = "buyer",fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private List<BuyerNotification> notifications;

    @PostPersist
    private void setRoleAndStatus() {
        role = Role.BUYER;
        status = ReportStatus.FREE;
    }

    //-----------------------------------------------

    @Builder
    public Buyer(String name, String email, String refreshToken, Gender gender, String phone, String profileImage, Provider provider, String socialId) {
        this.name = name;
        this.email = email;
        this.refreshToken = refreshToken;
        this.gender = gender;
        this.phone = phone;
        this.profileImage = profileImage;
        this.role = Role.BUYER;
        this.provider = provider;
        this.socialId = socialId;
    }

    public void login(String refreshToken) {
        this.isLogin = true;
        this.refreshToken = refreshToken;
    }

    public void logout() {
        this.refreshToken = null;
        this.isLogin = false;
    }

    public void changeStatus(ReportStatus newStatus) {
        status = newStatus;
    }

    public void updateBuyerInfo(String name) {
        this.name = name;
    }

    public void updateBuyerProfile(String profile) {
        this.profileImage = profile;
    }
}