package umc.unimade.domain.products.repository;

import umc.unimade.domain.products.entity.Products;
import umc.unimade.domain.products.entity.SortType;

import java.util.List;

public interface ProductsRepositoryCustom {
    List<Products> findProductsByFilters(List<Long> categoryIds, String keyword, Long minPrice, Long maxPrice, SortType sort, String cursor, int pageSize);
}
