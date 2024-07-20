package umc.unimade.domain.qna.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import umc.unimade.domain.qna.entity.Answers;


public interface AnswersRespository extends JpaRepository<Answers, Long> {
}
