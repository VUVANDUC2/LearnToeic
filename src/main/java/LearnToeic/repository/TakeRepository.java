package LearnToeic.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import LearnToeic.entity.Take;

public interface  TakeRepository extends JpaRepository<Take, Long>{
    Optional<Take> findTopByUser_UserIdAndTest_TestIdOrderByAttemptNoDesc(Integer userId, Integer testId);

    Optional<Take> findFirstByUser_UserIdAndTest_TestIdAndStatusOrderByAttemptNoDesc(
        Integer userId,
        Integer testId,
        String status
    );
    Optional<Take> findByTakeIdAndUser_UserId(Long takeId, Integer userId);

}
