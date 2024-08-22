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
    public ProductsListResponse findProductsByFilters(List<Long> categoryIds, String keyword, Long minPrice, Long maxPrice, SortType sort, String cursor, int pageSize) {
        List<Products> products = productRepository.findProductsByFilters(categoryIds, keyword, minPrice, maxPrice, sort, cursor, pageSize);

        Object nextCursor = determineNextCursor(products, sort);
        boolean isLast = products.size() < pageSize;

        return ProductsListResponse.from(products, nextCursor, isLast);
    }

    private String determineNextCursor(List<Products> products, SortType sort) {
        if (products.isEmpty()) {
            return null;
        }

        Products lastProduct = products.get(products.size() - 1);
        String cursorValue;
        String cursorId = String.valueOf(lastProduct.getId());

        cursorValue = switch (sort) {
            case FAVORITE -> String.valueOf(lastProduct.getTotalFavorite());
            case LATEST -> "nextCursor";
            case DEADLINE -> String.valueOf(lastProduct.getDeadline());
        };

        return cursorValue + "_" + cursorId;
    }


    private Products findProductById(Long productId){
        return productRepository.findById(productId)
                .orElseThrow(() -> new ProductsExceptionHandler(ErrorCode.PRODUCT_NOT_FOUND));
    }
}
