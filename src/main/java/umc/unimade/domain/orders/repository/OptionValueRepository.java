package umc.unimade.domain.orders.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import umc.unimade.domain.products.entity.OptionValue;

import java.util.List;
@Repository
public interface OptionValueRepository extends JpaRepository<OptionValue, Long> {

    void deleteAllByCategoryId(Long id);
}
