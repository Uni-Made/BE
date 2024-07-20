package umc.unimade.domain.accounts.service;


import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import umc.unimade.domain.accounts.dto.SellerRegisterResponse;
import umc.unimade.domain.accounts.dto.SellerRegistersResponse;
import umc.unimade.domain.accounts.entity.SellerRegister;
import umc.unimade.domain.accounts.exception.SellerExceptionHandler;
import umc.unimade.domain.accounts.repository.SellerRegisterRepository;
import umc.unimade.global.common.ErrorCode;
import umc.unimade.global.registerStatus.RegisterStatus;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class SellerRegisterQueryService {

    private final SellerRegisterRepository sellerRegisterRepository;

    public Page<SellerRegistersResponse> getSellersRegisters(Pageable pageable, RegisterStatus status) {

        Page<SellerRegister> sellerRegisters;

        if (status == null) {
            sellerRegisters = sellerRegisterRepository.findAll(pageable);
        } else {
            sellerRegisters = sellerRegisterRepository.findByRegisterStatus(status, pageable);
        }
        return sellerRegisters.map(SellerRegistersResponse::from);
    }

    public SellerRegisterResponse getSellerRegister(Long sellerRegisterId) {

        SellerRegister sellerRegister = sellerRegisterRepository.findById(sellerRegisterId)
                .orElseThrow(() -> new SellerExceptionHandler(ErrorCode.SELLER_NOT_FOUND));

        return SellerRegisterResponse.from(sellerRegister);
    }
}
