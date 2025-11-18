package LearnToeic.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import LearnToeic.entity.Take;

public interface  TakeRepository extends JpaRepository<Take, Integer>{
    // Optional<Take> findTopByUser_UserIdAndTest_TestIdOrderByAttemptNoDesc(Integer userId, Integer testId);

    // Optional<Take> findFirstByUser_UserIdAndTest_TestIdAndStatusOrderByAttemptNoDesc(
    //     Integer userId,
    //     Integer testId,
    //     String status
    // );
    // Optional<Take> findByTakeIdAndUser_UserId(Long takeId, Integer userId);
        @Query("select t.test.id from Take t where t.id = :takeId")
        Integer findTestIdByTakeId(Integer takeId);
        Optional<Take> findFirstByUser_UserIdAndTest_TestIdAndStatusOrderByAttemptNoDesc(
            Integer userId, Integer testId, String status);

         Optional<Take> findTopByUser_UserIdAndTest_TestIdOrderByAttemptNoDesc(
            Integer userId, Integer testId);

        List<Take> findByUser_UserIdAndStatusOrderByStartTimeDesc(Integer userId, String status);
}
