package umc.unimade.domain.qna.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import umc.unimade.domain.qna.dto.AnswerResponse;
import umc.unimade.domain.qna.dto.QuestionResponse;
import umc.unimade.domain.qna.entity.Answers;
import umc.unimade.domain.qna.entity.Questions;
import umc.unimade.domain.qna.repository.AnswersRespository;
import umc.unimade.domain.qna.repository.QuestionsRepository;
import umc.unimade.global.common.ErrorCode;
import umc.unimade.domain.qna.exception.QnAExceptionHandler;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class QnAQueryService {
    private final QuestionsRepository questionsRepository;
    private final AnswersRespository answersRepository;

    @Transactional
    public QuestionResponse getQuestion(Long questionId){

        Questions question = questionsRepository.findById(questionId)
                .orElseThrow(()-> new QnAExceptionHandler(ErrorCode.QNA_NOT_FOUND));
        return QuestionResponse.from(question);
    }

    @Transactional
    public AnswerResponse getAnswer(Long answerId){

        Answers answer = answersRepository.findById(answerId)
                .orElseThrow(()-> new QnAExceptionHandler(ErrorCode.QNA_NOT_FOUND));
        return AnswerResponse.from(answer);
    }
}
