package LearnToeic.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import LearnToeic.entity.TestResult;


public interface TestResultRepository extends JpaRepository<TestResult,Long>{
    // TestResult findByTake_TakeId(Integer takeId);
    Optional<TestResult> findByTake_TakeId(Integer takeId);
}
