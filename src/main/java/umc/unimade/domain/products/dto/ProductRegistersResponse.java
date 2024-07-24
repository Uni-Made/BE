package umc.unimade.domain.products.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import umc.unimade.domain.products.entity.ProductRegister;
import umc.unimade.domain.products.entity.RegisterType;
import umc.unimade.global.registerStatus.RegisterStatus;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ProductRegistersResponse {

    private Long productRegisterId;
    private String name;
    private RegisterStatus status;
    private RegisterType type;

    public static ProductRegistersResponse from(ProductRegister productRegister) {
        return ProductRegistersResponse.builder()
                .productRegisterId(productRegister.getId())
                .name(productRegister.getName())
                .status(productRegister.getRegisterStatus())
                .type(productRegister.getType())
                .build();
    }
}
