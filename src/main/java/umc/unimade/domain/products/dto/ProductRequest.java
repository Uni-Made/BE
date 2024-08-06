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
    public static class CreateProductDto {
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
        private Long categoryId;
        private List<OptionCategoryRequest> options;
        private LocalDate pickupDate;
        private String pickupLocation;

        public ProductRegister toEntity(Category category, Seller seller) {

            return ProductRegister.builder()
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
                    .type(RegisterType.REGISTER)
                    .seller(seller)
                    .category(category)
                    .pickupDate(pickupOption == PickupOption.OFFLINE ? pickupDate : null)
                    .pickupLocation(pickupOption == PickupOption.OFFLINE ? pickupLocation : null)
                    .build();
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
        private RegisterStatus registerStatus;
        private Long categoryId;
        private List<OptionCategoryRequest> options;
        private LocalDate pickupDate;
        private String pickupLocation;

        public ProductRegister toEntity(Category category, Seller seller, Long productId) {

            return ProductRegister.builder()
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
                    .type(RegisterType.UPDATE)
                    .seller(seller)
                    .category(category)
                    .productId(productId)
                    .pickupDate(pickupOption == PickupOption.OFFLINE ? pickupDate : null)
                    .pickupLocation(pickupOption == PickupOption.OFFLINE ? pickupLocation : null)
                    .build();
        }
    }
}