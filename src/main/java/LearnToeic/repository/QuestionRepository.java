package LearnToeic.repository;

import LearnToeic.model.Question;
import LearnToeic.model.QuestionId;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface QuestionRepository extends JpaRepository<Question, QuestionId> {

	java.util.List<Question> findByTestId(Integer testId);

}