package umc.unimade.domain.products.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import umc.unimade.domain.accounts.dto.AdminRegisterRequest;
import umc.unimade.domain.orders.repository.OptionValueRepository;
import umc.unimade.domain.products.dto.AdminProductRegisterResponse;
import umc.unimade.domain.products.entity.*;
import umc.unimade.domain.products.repository.OptionCategoryRepository;
import umc.unimade.domain.products.repository.ProductRegisterRepository;
import umc.unimade.domain.products.repository.ProductRepository;
import umc.unimade.domain.products.repository.ProductsImageRepository;
import umc.unimade.global.common.ErrorCode;
import umc.unimade.domain.products.exception.ProductsExceptionHandler;
import umc.unimade.global.registerStatus.RegisterStatus;

import java.util.ArrayList;
import java.util.List;

@RequiredArgsConstructor
@Transactional
@Service
public class ProductRegisterCommandService {

    private final ProductRegisterRepository productRegisterRepository;
    private final ProductRepository productRepository;
    private final OptionValueRepository optionValueRepository;
    private final OptionCategoryRepository optionCategoryRepository;
    private final ProductsImageRepository productsImageRepository;

    public AdminProductRegisterResponse approveProduct(Long productRegisterId, RegisterStatus registerStatus) {

        ProductRegister productRegister = productRegisterRepository.findById(productRegisterId)
                .orElseThrow(() -> new ProductsExceptionHandler(ErrorCode.PRODUCT_NOT_FOUND));

        if (!productRegister.getRegisterStatus().equals(RegisterStatus.PENDING) &&
                !productRegister.getRegisterStatus().equals(RegisterStatus.HOLD)) {
            throw new ProductsExceptionHandler(ErrorCode.PRODUCT_STATUS_IS_NOT_PENDING_OR_HOLD);
        }

        Products product = convertToProduct(productRegister);
        productRepository.save(product);

        productRegister.changeStatus(registerStatus);
        productRegister.setProductId(product.getId());

        return AdminProductRegisterResponse.of(productRegister, product);
    }

    public AdminProductRegisterResponse rejectOrHoldProduct(Long productRegisterId, RegisterStatus registerStatus, AdminRegisterRequest request) {

        ProductRegister productRegister = productRegisterRepository.findById(productRegisterId)
                .orElseThrow(() -> new ProductsExceptionHandler(ErrorCode.PRODUCT_NOT_FOUND));

        if (!productRegister.getRegisterStatus().equals(RegisterStatus.PENDING)) {
            throw new ProductsExceptionHandler(ErrorCode.PRODUCT_STATUS_IS_NOT_PENDING);
        }

        productRegister.changeStatus(registerStatus);
        productRegister.setReason(request.getReason());

        return AdminProductRegisterResponse.of(productRegister);
    }


    public AdminProductRegisterResponse approveUpdateProduct(Long productRegisterId, RegisterStatus registerStatus) {

        ProductRegister productRegister = productRegisterRepository.findById(productRegisterId)
                .orElseThrow(() -> new ProductsExceptionHandler(ErrorCode.PRODUCT_NOT_FOUND));

        if (!productRegister.getRegisterStatus().equals(RegisterStatus.PENDING) &&
                !productRegister.getRegisterStatus().equals(RegisterStatus.HOLD)) {
            throw new ProductsExceptionHandler(ErrorCode.PRODUCT_STATUS_IS_NOT_PENDING_OR_HOLD);
        }

        Products product = productRepository.findById(productRegister.getProductId())
                .orElseThrow(() -> new ProductsExceptionHandler(ErrorCode.PRODUCT_NOT_FOUND));

        product.updateProduct(productRegister);
        productRegister.changeStatus(registerStatus);

        // 기존 옵션 카테고리 및 옵션 값 제거
        for (OptionCategory category : product.getOptionCategories()) {
            optionValueRepository.deleteAllByCategoryId(category.getId());
        }
        optionCategoryRepository.deleteAllByProductId(product.getId());
        product.getOptionCategories().clear();

        for (OptionCategory category : productRegister.getOptionCategories()) {
            category.setProduct(product);
            product.getOptionCategories().add(category);
        }

        // 기존 이미지 제거
        productsImageRepository.deleteAllByProductId(product.getId());
        product.getProductImages().clear();

        for (ProductsImage image : productRegister.getProductImages()) {
            image.setProduct(product);
            product.getProductImages().add(image);
        }

        productRepository.save(product);

        return AdminProductRegisterResponse.of(productRegister, product);
    }

    private Products convertToProduct(ProductRegister productRegister) {

        Products product = Products.builder()
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
                .seller(productRegister.getSeller())
                .category(productRegister.getCategory())
                .build();

        // OptionCategory 연관관계 설정
        List<OptionCategory> categories = new ArrayList<>();
        for (OptionCategory category : productRegister.getOptionCategories()) {
            category.setProduct(product);
            if (!categories.contains(category)) {
                categories.add(category);
            }
        }
        product.setOptionCategories(categories);

        // ProductsImage 연관관계 설정
        List<ProductsImage> images = new ArrayList<>();
        for (ProductsImage image : productRegister.getProductImages()) {
            image.setProduct(product);
            if (!images.contains(image)) {
                images.add(image);
            }
        }
        product.setProductImages(images);

        return product;
    }
}
