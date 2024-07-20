package umc.unimade.domain.products.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import umc.unimade.domain.products.entity.*;
import umc.unimade.global.registerStatus.RegisterStatus;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ProductRegisterResponse {

    private Long id;
    private String name;
    private String content;
    private Long price;
    private LocalDate deadline;
    private ProductStatus status;
    private String university;
    private PickupOption pickupOption;
    private String bankName;
    private String accountNumber;
    private String accountName;
    private RegisterStatus registerStatus;
    private String reason;
    private Long categoryId;
    private List<ProductsImageDTO> productImages;

    public static ProductRegisterResponse from(ProductRegister productRegister) {
        return ProductRegisterResponse.builder()
                .id(productRegister.getId())
                .name(productRegister.getName())
                .content(productRegister.getContent())
                .price(productRegister.getPrice())
                .deadline(productRegister.getDeadline())
                .status(productRegister.getStatus())
                .university(productRegister.getUniversity())
                .pickupOption(productRegister.getPickupOption())
                .bankName(productRegister.getBankName())
                .accountNumber(productRegister.getAccountNumber())
                .accountName(productRegister.getAccountName())
                .registerStatus(productRegister.getRegisterStatus())
                .reason(productRegister.getReason())
                .categoryId(productRegister.getCategory().getId())
                .productImages(productRegister.getProductImages().stream()
                        .map(ProductsImageDTO::from)
                        .collect(Collectors.toList()))
                .build();
    }
}
