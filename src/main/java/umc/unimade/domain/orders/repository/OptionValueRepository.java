package umc.unimade.domain.orders.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import umc.unimade.domain.products.entity.OptionValue;

import java.util.List;
@Repository
public interface OptionValueRepository extends JpaRepository<OptionValue, Long> {

    void deleteAllByCategoryId(Long id);
}
