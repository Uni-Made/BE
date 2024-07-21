package umc.unimade.domain.products.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import umc.unimade.domain.accounts.entity.Seller;
import umc.unimade.domain.products.entity.*;
import umc.unimade.global.registerStatus.RegisterStatus;

import java.time.LocalDate;
import java.util.List;

public class ProductRequest {

    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class CreateProductDto { // TODO - 셀러 추가
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
        private Long sellerId;
        private Long categoryId;
        private List<OptionCategoryRequest> options;

        public ProductRegister toEntity(Category category, Seller seller) {
            ProductRegister product = ProductRegister.builder()
                    .name(name)
                    .content(content)
                    .price(price)
                    .deadline(deadline)
                    .status(status)
                    .university(university)
                    .pickupOption(pickupOption)
                    .bankName(bankName)
                    .accountNumber(accountNumber)
                    .accountName(accountName)
                    .registerStatus(registerStatus)
                    .seller(seller)
                    .category(category)
                    .build();

            return product;
        }
    }

    // 수정 dto
    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class UpdateProductDto {
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
        private Long categoryId;
//        private List<OptionRequest> options;
    }
}