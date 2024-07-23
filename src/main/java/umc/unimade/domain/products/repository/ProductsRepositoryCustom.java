package umc.unimade.domain.products.repository;

import umc.unimade.domain.products.entity.Products;

import java.util.List;

public interface ProductsRepositoryCustom {
    List<Products> findProductsByFilters(String category, String keyword, Long minPrice, Long maxPrice, String sort, Long cursor, int pageSize);
}
