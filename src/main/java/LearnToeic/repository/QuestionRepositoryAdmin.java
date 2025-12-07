package LearnToeic.repository;

import LearnToeic.model.QuestionAdmin;
import LearnToeic.model.QuestionId;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface QuestionRepositoryAdmin extends JpaRepository<QuestionAdmin, QuestionId> {

	java.util.List<QuestionAdmin> findByTestId(Integer testId);

}