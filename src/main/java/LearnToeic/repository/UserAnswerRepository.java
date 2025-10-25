package LearnToeic.repository;

import java.util.*;

import org.springframework.data.jpa.repository.JpaRepository;
import LearnToeic.entity.UserAnswer;
import LearnToeic.entity.UserAnswerId;

public interface  UserAnswerRepository extends JpaRepository<UserAnswer, UserAnswerId>{
    List<UserAnswer>findById_TakeId(Long takeId);
    Optional<UserAnswer>findById_TakeIdAndId_QuestionNumber(Long takeId, Integer qn);
    List<UserAnswer>findByTake_TakeId(Long takeId);
    //List<UserAnswer>findBy_TakeId(Long takeId);
    int updateAnswer(Long takeId, Integer qNo, Character opt, Boolean correct);
    List<UserAnswer>findById_TakeIdOrderById_QuestionNumberAsc(Long  takeId);
}
