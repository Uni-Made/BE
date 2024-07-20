package umc.unimade.domain.products.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import umc.unimade.domain.products.entity.ProductRegister;
import umc.unimade.global.registerStatus.RegisterStatus;

public interface ProductRegisterRepository extends JpaRepository<ProductRegister, Long> {
    Page<ProductRegister> findByRegisterStatus(RegisterStatus status, Pageable pageable);
}
