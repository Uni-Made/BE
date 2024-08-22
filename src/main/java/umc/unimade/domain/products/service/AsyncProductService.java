package umc.unimade.domain.products.service;

import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import umc.unimade.domain.products.entity.Products;
import umc.unimade.domain.products.exception.ProductsExceptionHandler;
import umc.unimade.domain.products.repository.ProductRepository;
import umc.unimade.global.common.ErrorCode;

@Service
@RequiredArgsConstructor
public class AsyncProductService {

    private final RedisTemplate<String, Integer> redisTemplate;
    private final ProductRepository productRepository;
    private static final String TOTAL_FAVORITE_KEY = "product:totalFavorite:";

    @Async
    @Transactional
    public void updateTotalFavorite(Long productId) {
        String key = TOTAL_FAVORITE_KEY + productId;
        Integer totalFavorite = redisTemplate.opsForValue().get(key);

        if (totalFavorite != null) {
            Products product = findProductById(productId);
            product.setTotalFavorite(totalFavorite);
            productRepository.save(product);
        }
    }
    private Products findProductById(Long productId){
        return productRepository.findById(productId)
                .orElseThrow(() -> new ProductsExceptionHandler(ErrorCode.PRODUCT_NOT_FOUND));
    }
}