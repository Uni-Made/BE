package umc.unimade.domain.products.service;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import umc.unimade.domain.products.ProductRepository;
import umc.unimade.domain.products.dto.ProductResponse;
import umc.unimade.domain.products.entity.Products;
import umc.unimade.domain.products.service.strategy.ProductStrategy;

import java.util.Map;

@Service
@RequiredArgsConstructor
public class ProductsQueryService {

    private final ProductRepository productRepository;
    private final Map<String, ProductStrategy> strategyMap;

    public ProductResponse getProduct(Long productId, String viewType, PageRequest pageRequest){
        Products product = findProductById(productId);

        ProductStrategy strategy = strategyMap.get(viewType + "Strategy");
        if (strategy == null) {
            throw new IllegalArgumentException("Invalid view type");
        }
        return strategy.loadProduct(product, pageRequest);
    }

    private Products findProductById(Long productId){
        return productRepository.findById(productId)
                .orElseThrow(() -> new IllegalArgumentException("Invalid product ID"));
    }
}
