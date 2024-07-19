package umc.unimade.domain.products.service.strategy;

import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Component;
import umc.unimade.domain.products.entity.Products;
import umc.unimade.domain.products.dto.ProductDetailResponse;
import umc.unimade.domain.products.dto.ProductResponse;

@Component("detailStrategy")
public class DetailStrategy implements ProductStrategy{
    @Override
    public ProductResponse loadProduct(Products product, PageRequest pageRequest){
        ProductResponse response = ProductResponse.to(product);
        ProductDetailResponse detailResponse = ProductDetailResponse.to(product);
        response.setDetail(detailResponse.getDetail());
        return response;

    }

}
