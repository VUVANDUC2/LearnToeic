package LearnToeic.repository;

import LearnToeic.entity.AnswerSheet;
import LearnToeic.entity.AnswerSheetId;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AnswerSheetRepository extends JpaRepository<AnswerSheet, AnswerSheetId> {
    List<AnswerSheet> findById_TestIdOrderById_Sequence(Integer testId);
    List<AnswerSheet> findById_TestId(Integer testId);
}
