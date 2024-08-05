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

    private Long productRegisterId;
    private String name;
    private String content;
    private Long price;
    private LocalDate deadline;
    private ProductStatus status;
    private String university;
    private PickupOption pickupOption;
    private LocalDate pickupDate;
    private String pickupLocation;
    private String bankName;
    private String accountNumber;
    private String accountName;
    private RegisterStatus registerStatus;
    private RegisterType registerType;
    private String reason;
    private Long categoryId;
    private List<ProductsImageDTO> productImages;
    private List<ProductDetailImageDTO> productDetailImages;
    private List<OptionCategoryResponse> optionCategories;

    public static ProductRegisterResponse from(ProductRegister productRegister) {
        return ProductRegisterResponse.builder()
                .productRegisterId(productRegister.getId())
                .name(productRegister.getName())
                .content(productRegister.getContent())
                .price(productRegister.getPrice())
                .deadline(productRegister.getDeadline())
                .status(productRegister.getStatus())
                .university(productRegister.getUniversity())
                .pickupOption(productRegister.getPickupOption())
                .pickupDate(productRegister.getPickupDate())
                .pickupLocation(productRegister.getPickupLocation())
                .bankName(productRegister.getBankName())
                .accountNumber(productRegister.getAccountNumber())
                .accountName(productRegister.getAccountName())
                .registerStatus(productRegister.getRegisterStatus())
                .registerType(productRegister.getType())
                .reason(productRegister.getReason())
                .categoryId(productRegister.getCategory().getId())
                .productImages(productRegister.getProductImages().stream()
                        .map(ProductsImageDTO::from)
                        .collect(Collectors.toList()))
                .productDetailImages(productRegister.getProductDetailImages().stream()
                        .map(ProductDetailImageDTO::from)
                        .collect(Collectors.toList()))
                .optionCategories(productRegister.getOptionCategories().stream()
                        .map(OptionCategoryResponse::from)
                        .collect(Collectors.toList()))
                .build();
    }
}
