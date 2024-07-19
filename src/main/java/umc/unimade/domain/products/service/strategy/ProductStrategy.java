package umc.unimade.domain.products.service.strategy;

import org.springframework.data.domain.PageRequest;
import umc.unimade.domain.products.dto.ProductResponse;
import umc.unimade.domain.products.entity.Products;

public interface ProductStrategy {
    ProductResponse loadProduct (Products product, PageRequest pageRequest);
}
