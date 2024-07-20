package umc.unimade.domain.accounts.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import umc.unimade.domain.accounts.entity.SellerRegister;
import umc.unimade.global.registerStatus.RegisterStatus;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SellerRegistersResponse {

    private Long sellerRegisterId;
    private String name;
    private RegisterStatus status;

    public static SellerRegistersResponse from(SellerRegister sellerRegister) {
        return SellerRegistersResponse.builder()
                .sellerRegisterId(sellerRegister.getId())
                .name(sellerRegister.getName())
                .status(sellerRegister.getRegisterStatus())
                .build();
    }
}