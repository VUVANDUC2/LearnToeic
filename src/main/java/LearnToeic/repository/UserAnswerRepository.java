package LearnToeic.repository;



import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import LearnToeic.entity.UserAnswer;

public interface  UserAnswerRepository extends JpaRepository<UserAnswer,Long>{
    List<UserAnswer>findByTake_TakeId(Integer takeId);
    int countByTake_TakeIdAndSelectedOptionIsNotNull(Integer takeId);
}
