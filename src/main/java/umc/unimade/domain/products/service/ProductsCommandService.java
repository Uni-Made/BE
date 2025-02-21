package umc.unimade.domain.products.service;

import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import umc.unimade.domain.accounts.entity.Buyer;
import umc.unimade.domain.accounts.entity.Seller;
import umc.unimade.domain.favorite.entity.FavoriteProduct;
import umc.unimade.domain.favorite.repository.FavoriteProductRepository;
import umc.unimade.domain.products.dto.OptionCategoryRequest;
import umc.unimade.domain.products.dto.ProductRegisterResponse;
import umc.unimade.domain.products.dto.ProductRequest.UpdateProductDto;
import umc.unimade.domain.products.dto.ProductRequest.CreateProductDto;
import umc.unimade.domain.products.dto.ProductResponse;
import umc.unimade.domain.products.dto.ProductUpdateResponse;
import umc.unimade.domain.products.entity.*;
import umc.unimade.domain.products.exception.ProductExceptionHandler;
import umc.unimade.domain.products.repository.*;
import umc.unimade.domain.products.repository.ProductRepository;
import umc.unimade.domain.products.entity.Products;
import umc.unimade.global.common.ApiResponse;
import umc.unimade.global.common.ErrorCode;

import umc.unimade.domain.products.exception.ProductsExceptionHandler;
import umc.unimade.global.util.s3.S3Provider;
import umc.unimade.global.util.s3.dto.S3UploadRequest;


import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional
public class ProductsCommandService {

    private final FavoriteProductRepository favoriteProductRepository;
    private final ProductRepository productRepository;
    private final ProductRegisterRepository productRegisterRepository;
    private final CategoryRepository categoryRepository;
    private final S3Provider s3Provider;
    private final RedisTemplate<String, Integer> redisTemplate;
    private final AsyncProductService asyncProductService;
    private static final String TOTAL_FAVORITE_KEY = "product:totalFavorite:";

    @Transactional
    public ApiResponse<Void> toggleFavoriteProduct(Long productId, Buyer buyer) {
        Products product = findProductById(productId);
        Optional<FavoriteProduct> existingFavorite = findFavoriteProduct(product, buyer);

        if (existingFavorite.isPresent()) {
            removeFavorite(existingFavorite.get(), productId);
            return ApiResponse.CANCELED_LIKE();
        } else {
            addFavorite(product, buyer, productId);
            return ApiResponse.SUCCESS_LIKE();
        }
    }

    private void removeFavorite(FavoriteProduct favoriteProduct, Long productId) {
        favoriteProductRepository.delete(favoriteProduct);
        updateFavoriteCount(productId, false);
    }

    private void addFavorite(Products product, Buyer buyer, Long productId) {
        FavoriteProduct favoriteProduct = FavoriteProduct.builder()
                .buyer(buyer)
                .product(product)
                .build();
        favoriteProductRepository.save(favoriteProduct);
        updateFavoriteCount(productId, true);
    }

    private void updateFavoriteCount(Long productId, boolean increment) {
        String key = TOTAL_FAVORITE_KEY + productId;
        if (increment) {
            redisTemplate.opsForValue().increment(key);
        } else {
            redisTemplate.opsForValue().decrement(key);
        }
        asyncProductService.updateTotalFavorite(productId);
    }

    private Products findProductById(Long productId){
        return productRepository.findById(productId)
                .orElseThrow(() -> new ProductsExceptionHandler(ErrorCode.PRODUCT_NOT_FOUND));
    }
    private Optional<FavoriteProduct> findFavoriteProduct(Products product, Buyer buyer) {
        return favoriteProductRepository.findByProductAndBuyer(product, buyer);
    }

    // 상품 등록
    @Transactional
    public ApiResponse<ProductRegisterResponse> createProduct(Seller seller, CreateProductDto request, List<MultipartFile> images, List<MultipartFile> detailImages) {
        Category category = categoryRepository.findById(request.getCategoryId())
                .orElseThrow(() -> new ProductExceptionHandler(ErrorCode.CATEGORY_NOT_FOUND));

        ProductRegister product = request.toEntity(category, seller);
        ProductRegister savedProduct = productRegisterRepository.save(product);

        // 옵션 카테고리 및 옵션 밸류 등록
        addOptionCategoriesToProduct(request.getOptions(), savedProduct);

        // 사진 등록
        addImagesToProduct(images, seller.getId(), savedProduct);

        // 상세 설명 사진 등록
        addDetailImagesToProduct(detailImages, seller.getId(), savedProduct);

        ProductRegisterResponse response = ProductRegisterResponse.from(savedProduct);
        return ApiResponse.onSuccess(response);
    }

    // 상품 수정
    @Transactional
    public ApiResponse<ProductUpdateResponse> updateProduct(Seller seller, Long productId, UpdateProductDto request, List<MultipartFile> images, List<MultipartFile> detailImages) {
        Products product = productRepository.findById(productId)
                .orElseThrow(() -> new ProductExceptionHandler(ErrorCode.PRODUCT_NOT_FOUND));

        if (product.getStatus() != ProductStatus.SELLING) {
            throw new ProductExceptionHandler(ErrorCode.PRODUCT_STATUS_IS_NOT_SELLING);
        }

        if (!product.getSeller().getId().equals(seller.getId())) {
            throw new ProductExceptionHandler(ErrorCode.PRODUCT_IS_NOT_YOURS);
        }

        Category category = categoryRepository.findById(request.getCategoryId())
                .orElseThrow(() -> new ProductExceptionHandler(ErrorCode.CATEGORY_NOT_FOUND));

        ProductRegister productRegister = request.toEntity(category, seller, productId);
        ProductRegister savedProduct = productRegisterRepository.save(productRegister);

        // 옵션 카테고리 및 옵션 밸류 등록
        addOptionCategoriesToProduct(request.getOptions(), savedProduct);

        // 사진 등록
        if (images != null && !images.isEmpty()) {
            addImagesToProduct(images, seller.getId(), savedProduct);
        } else {
            productRegister.setProductImages(new ArrayList<>(product.getProductImages()));
        }

        if (detailImages != null && !detailImages.isEmpty()) {
            addDetailImagesToProduct(detailImages, seller.getId(), savedProduct);
        } else {
            productRegister.setProductDetailImages(new ArrayList<>(product.getProductDetailImages()));
        }

        ProductUpdateResponse response = ProductUpdateResponse.from(savedProduct);
        return ApiResponse.onSuccess(response);
    }

    // 상품 삭제
    public void deleteProduct(Seller seller, Long productId) {

        Products product = productRepository.findById(productId)
                .orElseThrow(() -> new ProductExceptionHandler(ErrorCode.PRODUCT_NOT_FOUND));

        if (!product.getSeller().getId().equals(seller.getId())) {
            throw new ProductExceptionHandler(ErrorCode.PRODUCT_IS_NOT_YOURS);
        }

        productRepository.deleteById(productId);
    }

    private void addOptionCategoriesToProduct(List<OptionCategoryRequest> optionRequests, ProductRegister product) {
        if (optionRequests != null && !optionRequests.isEmpty()) {
            List<OptionCategory> optionCategories = optionRequests.stream()
                    .map(optionCategoryRequest -> {
                        OptionCategory optionCategory = OptionCategory.builder()
                                .name(optionCategoryRequest.getName())
                                .productRegister(product)
                                .build();
                        List<OptionValue> optionValues = optionCategoryRequest.getValues().stream()
                                .map(value -> OptionValue.builder()
                                        .value(value)
                                        .category(optionCategory)
                                        .build())
                                .collect(Collectors.toList());
                        optionCategory.setValues(optionValues);
                        return optionCategory;
                    })
                    .collect(Collectors.toList());
            product.setOptionCategories(optionCategories);
        }
    }

    private void addImagesToProduct(List<MultipartFile> images, Long sellerId, ProductRegister product) {
        if (images != null && !images.isEmpty()) {
            List<ProductsImage> productsImages = images.stream()
                    .map(image -> {
                        String imageUrl = s3Provider.uploadFile(image,
                                S3UploadRequest.builder()
                                        .userId(sellerId)
                                        .dirName("product")
                                        .build());
                        return ProductsImage.builder()
                                .imageUrl(imageUrl)
                                .productRegister(product)
                                .build();
                    })
                    .collect(Collectors.toList());
            product.setProductImages(productsImages);
        }
    }

    private void addDetailImagesToProduct(List<MultipartFile> images, Long sellerId, ProductRegister product) {
        if (images != null && !images.isEmpty()) {
            List<ProductDetailImage> productDetailImages = images.stream()
                    .map(image -> {
                        String imageUrl = s3Provider.uploadFile(image,
                                S3UploadRequest.builder()
                                        .userId(sellerId)
                                        .dirName("product")
                                        .build());
                        return ProductDetailImage.builder()
                                .imageUrl(imageUrl)
                                .productRegister(product)
                                .build();
                    })
                    .collect(Collectors.toList());
            product.setProductDetailImages(productDetailImages);
        }
        else {
            product.setProductDetailImages(new ArrayList<>());
        }
    }

    // 상품 deadline 지나면 soldout으로 status 변경
    @Scheduled(cron = "0 0 0 * * *") // 0시 0분 0초
    @Transactional
    public void updateProductStatus() {
        List<Products> expiredProducts = productRepository.findExpiredProducts(ProductStatus.SELLING);

        for (Products product : expiredProducts) {
            product.setStatus(ProductStatus.SOLDOUT);
        }

         productRepository.saveAll(expiredProducts);
    }

    // 판매 중단 상품 판매 재등록
    @Transactional
    public ApiResponse<ProductResponse> resaleProduct(Long productId) {
        Products product = productRepository.findById(productId)
                .orElseThrow(() -> new ProductExceptionHandler(ErrorCode.PRODUCT_NOT_FOUND));

        if (product.getStatus() != ProductStatus.SOLDOUT) {
            throw new ProductExceptionHandler(ErrorCode.PRODUCT_STATUS_IS_NOT_SOLDOUT);
        }

        product.setStatus(ProductStatus.SELLING);
        product.setCreatedAt(LocalDateTime.now().plusDays(1));
        Products savedProduct = productRepository.save(product);

        ProductResponse response = ProductResponse.baseResponse(savedProduct);
        return ApiResponse.onSuccess(response);
    }
}
