package umc.unimade.domain.accounts.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import umc.unimade.domain.accounts.dto.AdminSellerRegisterRequest;
import umc.unimade.domain.accounts.dto.AdminSellerRegisterResponse;
import umc.unimade.domain.accounts.entity.Seller;
import umc.unimade.domain.accounts.entity.SellerRegister;
import umc.unimade.domain.accounts.exception.SellerExceptionHandler;
import umc.unimade.domain.accounts.repository.SellerRegisterRepository;
import umc.unimade.domain.accounts.repository.SellerRepository;
import umc.unimade.global.common.ErrorCode;
import umc.unimade.global.registerStatus.RegisterStatus;

@RequiredArgsConstructor
@Transactional
@Service
public class SellerRegisterCommandService {

    private final SellerRegisterRepository sellerRegisterRepository;
    private final SellerRepository sellerRepository;

    public AdminSellerRegisterResponse approveSeller(Long sellerRegisterId, RegisterStatus registerStatus) {

        SellerRegister sellerRegister = sellerRegisterRepository.findById(sellerRegisterId)
                .orElseThrow(() -> new SellerExceptionHandler(ErrorCode.SELLER_NOT_FOUND));

        if (!sellerRegister.getRegisterStatus().equals(RegisterStatus.PENDING) &&
                !sellerRegister.getRegisterStatus().equals(RegisterStatus.HOLD)) {
            throw new SellerExceptionHandler(ErrorCode.STATUS_IS_NOT_PENDING_OR_HOLD);
        }

        Seller seller = convertToSeller(sellerRegister);
        sellerRepository.save(seller);

        sellerRegister.changeStatus(registerStatus);

        return AdminSellerRegisterResponse.of(sellerRegister, seller);
    }

    public AdminSellerRegisterResponse rejectOrHoldSeller(Long sellerRegisterId, RegisterStatus registerStatus, AdminSellerRegisterRequest request) {

        SellerRegister sellerRegister = sellerRegisterRepository.findById(sellerRegisterId)
                .orElseThrow(() -> new SellerExceptionHandler(ErrorCode.SELLER_NOT_FOUND));

        if (!sellerRegister.getRegisterStatus().equals(RegisterStatus.PENDING)) {
            throw new SellerExceptionHandler(ErrorCode.STATUS_IS_NOT_PENDING);
        }

        sellerRegister.changeStatus(registerStatus);
        sellerRegister.setReason(request.getReason());

        return AdminSellerRegisterResponse.of(sellerRegister);
    }

    // TODO: sellerRequest 로 옮기기
    private Seller convertToSeller(SellerRegister sellerRegister) {
        return Seller.builder()
                .name(sellerRegister.getName())
                .email(sellerRegister.getEmail())
                .password(sellerRegister.getPassword())
                .gender(sellerRegister.getGender())
                .phone(sellerRegister.getPhone())
                .profileImage(sellerRegister.getProfileImage())
                .provider(sellerRegister.getProvider())
                .build();
    }
}
