package umc.unimade.domain.products;

import org.springframework.data.jpa.repository.JpaRepository;
import umc.unimade.domain.products.entity.Products;

public interface repository extends JpaRepository<Products, Long> {
}
