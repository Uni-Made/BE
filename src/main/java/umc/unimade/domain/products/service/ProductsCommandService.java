package umc.unimade.domain.products.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import umc.unimade.domain.accounts.entity.Buyer;
import umc.unimade.domain.accounts.entity.Seller;
import umc.unimade.domain.accounts.repository.BuyerRepository;
import umc.unimade.domain.accounts.repository.SellerRepository;
import umc.unimade.domain.favorite.entity.FavoriteProduct;
import umc.unimade.domain.favorite.repository.FavoriteProductRepository;
import umc.unimade.domain.favorite.repository.FavoriteSellerRepository;
import umc.unimade.domain.products.dto.ProductRequest.UpdateProductDto;
import umc.unimade.domain.products.dto.ProductRequest.CreateProductDto;
import umc.unimade.domain.products.entity.*;
import umc.unimade.domain.products.exception.ProductExceptionHandler;
import umc.unimade.domain.products.repository.*;
import umc.unimade.domain.products.repository.ProductRepository;
import umc.unimade.domain.products.entity.Products;
import umc.unimade.global.common.ApiResponse;
import umc.unimade.global.common.ErrorCode;

import umc.unimade.domain.products.exception.ProductsExceptionHandler;
import umc.unimade.domain.accounts.exception.UserExceptionHandler;
import umc.unimade.global.util.s3.S3Provider;
import umc.unimade.global.util.s3.dto.S3UploadRequest;


import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional
public class ProductsCommandService {

    private final FavoriteProductRepository favoriteProductRepository;
    private final FavoriteSellerRepository favoriteSellerRepository;
    private final ProductRepository productRepository;
    private final ProductRegisterRepository productRegisterRepository;
    private final BuyerRepository buyerRepository;
    private final CategoryRepository categoryRepository;
//    private final OptionsRepository optionsRepository;
    private final ProductsImageRepository productsImageRepository;
    private final S3Provider s3Provider;
    private final SellerRepository sellerRepository;

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

    // 상품 등록
    // TODO - seller 추가
    @Transactional
    public ApiResponse<ProductRegister> createProduct(CreateProductDto request, List<MultipartFile> images) {
        Category category = categoryRepository.findById(request.getCategoryId())
                // TODO 에러 핸들러
                .orElseThrow(() -> new ProductExceptionHandler(ErrorCode.CATEGORY_NOT_FOUND));
        Seller seller = sellerRepository.findById(request.getSellerId())
                .orElseThrow(() -> new ProductExceptionHandler(ErrorCode.SELLER_NOT_FOUND));

        ProductRegister product = request.toEntity(category, seller);
        ProductRegister savedProduct = productRegisterRepository.save(product);

        // 옵션 등록
//        List<Options> options = request.getOptions().stream()
//                .map(optionRequest -> optionRequest.toEntity(savedProduct))
//                .collect(Collectors.toList());
//
//        optionsRepository.saveAll(options);
//        savedProduct.setOptions(options);

        // 사진 등록
        if (images != null && !images.isEmpty()) {
            List<ProductsImage> productsImages = images.stream()
                    .map(image -> {
                        String imageUrl = s3Provider.uploadFile(image,
                                S3UploadRequest.builder()
                                        .userId(seller.getId())
                                        .dirName("product")
                                        .build());
                        return ProductsImage.builder()
                                .imageUrl(imageUrl)
                                .productRegister(savedProduct)
                                .build();
                    })
                    .collect(Collectors.toList());
            savedProduct.setProductImages(productsImages);
        }
        return ApiResponse.onSuccess(savedProduct);
    }

    // 상품 수정
    @Transactional
    public ApiResponse<Products> updateProduct(Long productId, UpdateProductDto request) {
        Products product = productRepository.findById(productId)
                .orElseThrow(() -> new ProductExceptionHandler(ErrorCode.PRODUCT_NOT_FOUND));;

        Category category = categoryRepository.findById(request.getCategoryId())
                .orElseThrow(() -> new ProductExceptionHandler(ErrorCode.CATEGORY_NOT_FOUND));

        product.updateProduct(request, category);

        // TODO - 옵션 수정 구현

        return ApiResponse.onSuccess(productRepository.save(product));
    }

    // 상품 삭제
    public void deleteProduct(Long productId) {

        Products product = productRepository.findById(productId)
                .orElseThrow(() -> new ProductExceptionHandler(ErrorCode.PRODUCT_NOT_FOUND));

        productRepository.deleteById(productId);
    }
}
