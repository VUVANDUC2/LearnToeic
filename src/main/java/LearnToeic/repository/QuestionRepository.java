package LearnToeic.repository;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import LearnToeic.entity.Question;
import LearnToeic.entity.QuestionId;


public interface QuestionRepository extends JpaRepository<Question, QuestionId> {
    // List<Question> findById_TestIdOrderById_QuestionNumber(Integer testId);
    // Optional<Question> findById_TestIdAndId_QuestionNumber(Integer testId, Integer qn);
    List<Question> findById_TestIdAndPartOrderById_QuestionNumber(Integer testId, int part);
    Integer countById_TestId(Integer testId);

    List<Question> findById_TestIdOrderById_QuestionNumber(Integer testId);
}
