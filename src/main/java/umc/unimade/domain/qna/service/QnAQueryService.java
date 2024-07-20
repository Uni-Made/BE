package umc.unimade.domain.qna.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import umc.unimade.domain.qna.dto.AnswerResponse;
import umc.unimade.domain.qna.dto.QuestionResponse;
import umc.unimade.domain.qna.repository.AnswersRespository;
import umc.unimade.domain.qna.repository.QuestionsRepository;
import umc.unimade.global.common.ErrorCode;
import umc.unimade.global.common.exception.QnAExceptionHandler;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class QnAQueryService {
    private final QuestionsRepository questionsRepository;
    private final AnswersRespository answersRespository;

    @Transactional
    public QuestionResponse getQuestion(Long questionId){
        return questionsRepository.findById(questionId)
                .map(QuestionResponse::from)
                .orElseThrow(()-> new QnAExceptionHandler(ErrorCode.QNA_NOT_FOUND));
    }

    @Transactional
    public AnswerResponse getAnswer(Long answerId){
        return answersRespository.findById(answerId)
                .map(AnswerResponse::from)
                .orElseThrow(()-> new QnAExceptionHandler(ErrorCode.QNA_NOT_FOUND));
    }
}
