package umc.unimade.domain.accounts.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import umc.unimade.domain.accounts.entity.Seller;
import umc.unimade.domain.accounts.entity.SellerRegister;
import umc.unimade.global.registerStatus.RegisterStatus;

import java.time.LocalDateTime;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@JsonInclude(JsonInclude.Include.NON_NULL)
public class AdminSellerRegisterResponse {

    private Long sellerRegisterId;
    private Long sellerId;
    private RegisterStatus status;
    private String reason;
    private LocalDateTime createdAt;

    public static AdminSellerRegisterResponse of(SellerRegister sellerRegister, Seller seller) {
        return AdminSellerRegisterResponse.builder()
                .sellerRegisterId(sellerRegister.getId())
                .sellerId(seller.getId())
                .status(sellerRegister.getRegisterStatus())
                .createdAt(LocalDateTime.now())
                .build();
    }

    public static AdminSellerRegisterResponse of(SellerRegister sellerRegister) {
        return AdminSellerRegisterResponse.builder()
                .sellerRegisterId(sellerRegister.getId())
                .status(sellerRegister.getRegisterStatus())
                .reason(sellerRegister.getReason())
                .createdAt(LocalDateTime.now())
                .build();
    }
}
