package umc.unimade.domain.accounts.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import umc.unimade.domain.accounts.entity.Seller;

@Getter
@Builder
@AllArgsConstructor
public class SellerInfoResponseDto {
    private String name;
    private String password;

    public static SellerInfoResponseDto of(Seller seller) {
        return SellerInfoResponseDto.builder()
                .name(seller.getName())
                .password(seller.getPassword())
                .build();
    }
}
