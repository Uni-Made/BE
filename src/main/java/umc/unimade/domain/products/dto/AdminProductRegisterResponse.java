package umc.unimade.domain.products.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import umc.unimade.domain.products.entity.ProductRegister;
import umc.unimade.domain.products.entity.Products;
import umc.unimade.domain.products.entity.RegisterType;
import umc.unimade.global.registerStatus.RegisterStatus;

import java.time.LocalDateTime;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@JsonInclude(JsonInclude.Include.NON_NULL)
public class AdminProductRegisterResponse {

    private Long productRegisterId;
    private Long productId;
    private RegisterStatus status;
    private RegisterType type;
    private String reason;
    private LocalDateTime createdAt;

    public static AdminProductRegisterResponse of(ProductRegister productRegister, Products product) {
        return AdminProductRegisterResponse.builder()
                .productRegisterId(productRegister.getId())
                .productId(product.getId())
                .status(productRegister.getRegisterStatus())
                .type(productRegister.getType())
                .createdAt(LocalDateTime.now())
                .build();
    }

    public static AdminProductRegisterResponse of(ProductRegister productRegister) {
        return AdminProductRegisterResponse.builder()
                .productRegisterId(productRegister.getId())
                .status(productRegister.getRegisterStatus())
                .type(productRegister.getType())
                .reason(productRegister.getReason())
                .createdAt(LocalDateTime.now())
                .build();
    }
}
