package umc.unimade.domain.products.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import umc.unimade.domain.products.entity.PickupOption;
import umc.unimade.domain.products.entity.ProductRegister;
import umc.unimade.domain.products.entity.Products;
import umc.unimade.domain.products.entity.ProductStatus;
import umc.unimade.global.registerStatus.RegisterStatus;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ProductUpdateResponse {

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
    private List<OptionCategoryResponse> optionCategories;

    public static ProductUpdateResponse from(ProductRegister product) {
        return ProductUpdateResponse.builder()
                .id(product.getId())
                .name(product.getName())
                .content(product.getContent())
                .price(product.getPrice())
                .deadline(product.getDeadline())
                .status(product.getStatus())
                .university(product.getUniversity())
                .pickupOption(product.getPickupOption())
                .bankName(product.getBankName())
                .accountNumber(product.getAccountNumber())
                .accountName(product.getAccountName())
                .categoryId(product.getCategory().getId())
                .productImages(product.getProductImages().stream()
                        .map(ProductsImageDTO::from)
                        .collect(Collectors.toList()))
                .optionCategories(product.getOptionCategories().stream()
                        .map(OptionCategoryResponse::from)
                        .collect(Collectors.toList()))
                .build();
    }
}
