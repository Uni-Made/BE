package umc.unimade.domain.products.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import umc.unimade.domain.products.entity.ProductRegister;

@Repository
public interface ProductRegisterRepository extends JpaRepository<ProductRegister, Long> {
}