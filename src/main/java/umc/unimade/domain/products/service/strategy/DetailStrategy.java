package umc.unimade.domain.products.service.strategy;

import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Component;
import umc.unimade.domain.products.entity.Products;
import umc.unimade.domain.products.dto.ProductDetailResponse;
import umc.unimade.domain.products.dto.ProductResponse;

@Component("detailStrategy")
public class DetailStrategy implements ProductStrategy{
    @Override
    public ProductResponse loadProduct(Products product, Long cursor, int pageSize){
        ProductResponse response = ProductResponse.from(product);
        ProductDetailResponse detailResponse = ProductDetailResponse.from(product);
        response.setDetail(detailResponse.getDetail());
        return response;

    }

}
