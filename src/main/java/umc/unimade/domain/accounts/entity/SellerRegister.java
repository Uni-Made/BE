package umc.unimade.domain.accounts.entity;

import jakarta.persistence.*;
import lombok.*;
import umc.unimade.global.registerStatus.RegisterStatus;

@Entity
@Builder
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Table(name = "seller_register")
public class SellerRegister {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "seller_register_id", nullable = false)
    private Long id;

    @Column(name = "name", nullable = false)
    private String name;

    @Column(name = "email", nullable = false)
    private String email;

    @Column(name = "password", nullable = false)
    private String password;

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

    @Enumerated(EnumType.STRING)
    @Column(name = "registerStatus", nullable = false)
    private RegisterStatus registerStatus;

    @Column(name = "reason")
    private String reason;  // 거부 or 보류 사유

    @PostPersist
    private void setRegisterStatus() {
        registerStatus = RegisterStatus.PENDING;
    }

    public void changeStatus(RegisterStatus registerStatus) {
        this.registerStatus = registerStatus;
    }

    public void setReason(String reason) {
        this.reason = reason;
    }
}