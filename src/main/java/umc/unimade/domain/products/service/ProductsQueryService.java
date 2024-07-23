package umc.unimade.domain.products.service;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import umc.unimade.domain.products.dto.ProductsListResponse;
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
    public ProductResponse getProduct(Long productId, ViewType viewType, PageRequest pageRequest){
        Products product = findProductById(productId);

        ProductStrategy strategy = strategyMap.get(viewType.name().toLowerCase() + "Strategy");
        if (strategy == null) {
            throw new IllegalArgumentException("Invalid view type");
        }
        return strategy.loadProduct(product, pageRequest);
    }

    @Transactional
    public ProductsListResponse findProductsByFilters(String category, String keyword, Long minPrice, Long maxPrice, String sort, Long cursor, int pageSize) {
        List<Products> products = productRepository.findProductsByFilters(category, keyword, minPrice, maxPrice, sort, cursor, pageSize);

        Long nextCursor = products.isEmpty() ? null : products.get(products.size() - 1).getId();
        boolean isLast = products.size() < pageSize;

        return ProductsListResponse.from(products, nextCursor, isLast);
    }

    private Products findProductById(Long productId){
        return productRepository.findById(productId)
                .orElseThrow(() -> new ProductsExceptionHandler(ErrorCode.PRODUCT_NOT_FOUND));
    }
}
