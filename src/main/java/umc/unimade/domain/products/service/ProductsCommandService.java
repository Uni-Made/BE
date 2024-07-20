package umc.unimade.domain.products.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import umc.unimade.domain.accounts.entity.Buyer;
import umc.unimade.domain.accounts.repository.BuyerRepository;
import umc.unimade.domain.favorite.entity.FavoriteProduct;
import umc.unimade.domain.favorite.repository.FavoriteProductRepository;
import umc.unimade.domain.favorite.repository.FavoriteSellerRepository;
import umc.unimade.domain.products.repository.ProductRepository;
import umc.unimade.domain.products.entity.Products;
import umc.unimade.global.common.ApiResponse;
import umc.unimade.global.common.ErrorCode;
import umc.unimade.global.common.exception.ProductsExceptionHandler;
import umc.unimade.global.common.exception.UserExceptionHandler;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class ProductsCommandService {

    private final FavoriteProductRepository favoriteProductRepository;
    private final FavoriteSellerRepository favoriteSellerRepository;
    private final ProductRepository productRepository;
    private final BuyerRepository buyerRepository;

    @Transactional
    public ApiResponse<Void> toggleFavoriteProduct(Long productId, Long buyerId) {
        Products product = findProductById(productId);
        Buyer buyer = findBuyerById(buyerId);
        Optional<FavoriteProduct> existingFavorite = findFavoriteProduct(product,buyer);

        if (existingFavorite.isPresent()) {
            favoriteProductRepository.delete(existingFavorite.get());
            return ApiResponse.CANCELED_LIKE();
        }else{
            FavoriteProduct favoriteProduct = FavoriteProduct.builder()
                    .buyer(buyer)
                    .product(product)
                    .build();
            favoriteProductRepository.save(favoriteProduct);
            return ApiResponse.SUCCESS_LIKE();
        }
    }



    private Buyer findBuyerById(Long buyerId) {
        return buyerRepository.findById(buyerId)
                .orElseThrow(() -> new UserExceptionHandler(ErrorCode.BUYER_NOT_FOUND));
    }
    private Products findProductById(Long productId){
        return productRepository.findById(productId)
                .orElseThrow(() -> new ProductsExceptionHandler(ErrorCode.PRODUCT_NOT_FOUND));
    }
    private Optional<FavoriteProduct> findFavoriteProduct(Products product, Buyer buyer) {
        return favoriteProductRepository.findByProductAndBuyer(product, buyer);
    }
}
