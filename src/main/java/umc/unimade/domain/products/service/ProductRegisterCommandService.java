package umc.unimade.domain.products.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import umc.unimade.domain.accounts.dto.AdminRegisterRequest;
import umc.unimade.domain.products.dto.AdminProductRegisterResponse;
import umc.unimade.domain.products.entity.ProductRegister;
import umc.unimade.domain.products.entity.Products;
import umc.unimade.domain.products.entity.ProductsImage;
import umc.unimade.domain.products.repository.ProductRegisterRepository;
import umc.unimade.domain.products.repository.ProductRepository;
import umc.unimade.global.common.ErrorCode;
import umc.unimade.domain.products.exception.ProductsExceptionHandler;
import umc.unimade.global.registerStatus.RegisterStatus;

import java.util.List;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Transactional
@Service
public class ProductRegisterCommandService {

    private final ProductRegisterRepository productRegisterRepository;
    private final ProductRepository productRepository;

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

    // TODO: productRequest 로 옮기기
    private Products convertToProduct(ProductRegister productRegister) {

        // 복사된 이미지들 생성
        List<ProductsImage> copiedProductImages = productRegister.getProductImages().stream()
                .map(image -> ProductsImage.builder()
                        .imageUrl(image.getImageUrl())
                        .product(null) // 이미지의 product 필드는 뒤에 설정
                        .build())
                .collect(Collectors.toList());

        Products product =  Products.builder()
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
                .category(productRegister.getCategory())
                .productImages(copiedProductImages)
                .build();

        // 복사한 ProductImage 객체들과 연관 설정
        for (ProductsImage image : copiedProductImages) {
            image.setProduct(product);
        }

        return product;
    }
}
