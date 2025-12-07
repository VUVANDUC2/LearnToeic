package LearnToeic.repository;

import LearnToeic.entity.*;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import java.util.List;
@Repository
public interface QuestionRepositoryAdmin extends JpaRepository<Question, QuestionId> {

    // Dùng composite key path
    List<Question> findById_TestIdOrderById_QuestionNumber(Integer testId);

}
