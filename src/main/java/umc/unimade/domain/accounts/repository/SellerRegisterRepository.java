package umc.unimade.domain.accounts.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import umc.unimade.domain.accounts.entity.SellerRegister;
import umc.unimade.global.registerStatus.RegisterStatus;

public interface SellerRegisterRepository extends JpaRepository<SellerRegister, Long> {
    Page<SellerRegister> findByRegisterStatus(RegisterStatus status, Pageable pageable);
}
