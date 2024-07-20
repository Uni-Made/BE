package umc.unimade.domain.products.service;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import umc.unimade.domain.products.dto.ProductRegisterResponse;
import umc.unimade.domain.products.dto.ProductRegistersResponse;
import umc.unimade.domain.products.entity.ProductRegister;
import umc.unimade.domain.products.repository.ProductRegisterRepository;
import umc.unimade.global.common.ErrorCode;
import umc.unimade.domain.products.exception.ProductsExceptionHandler;
import umc.unimade.global.registerStatus.RegisterStatus;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class ProductRegisterQueryService {

    private final ProductRegisterRepository productRegisterRepository;

    public Page<ProductRegistersResponse> getProductsRegisters(Pageable pageable, RegisterStatus status) {

        Page<ProductRegister> productRegisters;

        if (status == null) {
            productRegisters = productRegisterRepository.findAll(pageable);
        } else {
            productRegisters = productRegisterRepository.findByRegisterStatus(status, pageable);
        }
        return productRegisters.map(ProductRegistersResponse::from);
    }

    public ProductRegisterResponse getProductRegister(Long productRegisterId) {

        ProductRegister productRegister = productRegisterRepository.findById(productRegisterId)
                .orElseThrow(() -> new ProductsExceptionHandler(ErrorCode.PRODUCT_NOT_FOUND));

        return ProductRegisterResponse.from(productRegister);
    }
}
