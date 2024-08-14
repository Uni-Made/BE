package umc.unimade.domain.products.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import umc.unimade.domain.products.dto.ProductsListResponse;
import umc.unimade.domain.products.entity.SortType;
import umc.unimade.domain.products.repository.ProductRepository;
import umc.unimade.domain.products.dto.ProductResponse;
import umc.unimade.domain.products.entity.Products;
import umc.unimade.domain.products.entity.ViewType;
import umc.unimade.domain.products.service.strategy.ProductStrategy;
import umc.unimade.global.common.ErrorCode;
import umc.unimade.domain.products.exception.ProductsExceptionHandler;

import java.util.List;
import java.util.Map;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class ProductsQueryService {

    private final ProductRepository productRepository;
    private final Map<String, ProductStrategy> strategyMap;

    @Transactional
    public ProductResponse getProduct(Long productId, ViewType viewType, Long cursor, int pageSize){
        Products product = findProductById(productId);
        ProductStrategy strategy = strategyMap.get(viewType.name().toLowerCase() + "Strategy");
        if (strategy == null) {
            throw new IllegalArgumentException("Invalid view type");
        }
        return strategy.loadProduct(product, cursor, pageSize);
    }

    @Transactional
    public ProductsListResponse findProductsByFilters(List<Long> categoryIds, String keyword, Long minPrice, Long maxPrice, SortType sort, int offset, int pageSize) {
        List<Products> products = productRepository.findProductsByFilters(categoryIds, keyword, minPrice, maxPrice, sort, offset, pageSize);
        boolean isLast = products.size() < pageSize;
        int nextOffset = offset + products.size();
        return ProductsListResponse.from(products, nextOffset, isLast);
    }

    private Products findProductById(Long productId){
        return productRepository.findById(productId)
                .orElseThrow(() -> new ProductsExceptionHandler(ErrorCode.PRODUCT_NOT_FOUND));
    }
}
