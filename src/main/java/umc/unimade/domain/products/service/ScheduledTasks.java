package umc.unimade.domain.products.service;

import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import umc.unimade.domain.products.entity.Products;
import umc.unimade.domain.products.repository.ProductRepository;

import java.util.List;

@EnableScheduling
@Service
@RequiredArgsConstructor
public class ScheduledTasks {

    private final AsyncProductService asyncProductService;
    private final ProductRepository productRepository;

    @Scheduled(fixedRate = 60000) // 60초마다 실행
    public void syncFavoriteCount() {
        List<Products> products = productRepository.findAll();
        for (Products product : products) {
            asyncProductService.updateTotalFavorite(product.getId());
        }
    }
}
