package umc.unimade.domain.products.service.strategy;

import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;
import org.springframework.data.domain.PageRequest;
import umc.unimade.domain.accounts.dto.BuyerOrderHistoryResponse;
import umc.unimade.domain.products.dto.ProductResponse;
import umc.unimade.domain.products.entity.Products;
import umc.unimade.domain.qna.dto.QnAListResponse;
import umc.unimade.domain.qna.entity.Questions;
import umc.unimade.domain.qna.repository.QuestionsRepository;

import lombok.*;
import java.util.List;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Component("qnaStrategy")
public class QnAStrategy implements ProductStrategy{
    private final QuestionsRepository questionsRepository;

    @Override
    public ProductResponse loadProduct(Products product, Long cursor, int pageSize) {
        ProductResponse response = ProductResponse.from(product);
        Pageable pageable = PageRequest.of(0, pageSize);
        List<Questions> questions = questionsRepository.findByProductIdWithCursorPagination(product.getId(), cursor, pageable);
        Long nextCursor = questions.isEmpty() ? null : questions.get(questions.size() - 1).getId();
        Boolean isLast = questions.size() < pageSize;

        QnAListResponse qnAListResponse = QnAListResponse.from(questions, nextCursor, isLast);

        response.setQuestions(qnAListResponse);
        return response;
    }
}
