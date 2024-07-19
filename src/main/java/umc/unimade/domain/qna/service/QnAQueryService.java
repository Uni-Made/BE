package umc.unimade.domain.qna.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import umc.unimade.domain.qna.dto.QuestionResponse;
import umc.unimade.domain.qna.repository.QuestionsRepository;
import umc.unimade.global.common.ErrorCode;
import umc.unimade.global.common.exception.QnAExceptionHandler;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class QnAQueryService {
    private final QuestionsRepository questionsRepository;

    @Transactional
    public QuestionResponse getQuestion(Long questionId){
        return questionsRepository.findById(questionId)
                .map(QuestionResponse::to)
                .orElseThrow(()-> new QnAExceptionHandler(ErrorCode.QNA_NOT_FOUND));
    }
}
