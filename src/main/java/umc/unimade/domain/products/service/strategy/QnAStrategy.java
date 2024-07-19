package umc.unimade.domain.products.service.strategy;

import org.springframework.stereotype.Component;
import org.springframework.data.domain.PageRequest;
import umc.unimade.domain.products.dto.ProductResponse;
import umc.unimade.domain.products.entity.Products;
import umc.unimade.domain.qna.dto.QnAListResponse;
import umc.unimade.domain.qna.repository.QuestionsRepository;

import lombok.*;
import java.util.List;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Component("qnaStrategy")
public class QnAStrategy implements ProductStrategy{
    private final QuestionsRepository questionsRepository;

    @Override
    public ProductResponse loadProduct(Products product, PageRequest pageRequest) {
        ProductResponse response = ProductResponse.to(product);
        List<QnAListResponse> questions = questionsRepository.findByProductId(product.getId(), pageRequest)
                .getContent().stream()
                .map(QnAListResponse::to)
                .collect(Collectors.toList());
        response.setQuestions(questions);
        return response;
    }
}
