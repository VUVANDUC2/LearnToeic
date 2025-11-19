package LearnToeic.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import LearnToeic.model.Take;
import java.util.List;
@Repository
public interface TakeRepository extends JpaRepository<Take, Integer> {
    @Query("SELECT t FROM Take t WHERE t.test.testId = :testId")
    List<Take> findAllByTestId(@Param("testId") Integer testId);

    @Query("SELECT t.test.id, AVG(t.score) FROM Take t GROUP BY t.test.id")
    List<Object[]> findAverageScorePerTest();


    @Query("SELECT t.user.userId, count(*) as total_attempts FROM Take t group by t.user.userId ORDER BY total_attempts DESC limit 5")
    List<Object[]> findUserAttemptsOrdered();
    

    @Query("SELECT MONTH(t.taken_on) as month, COUNT(*) from Take t Group by month ORDER BY month")
    List<Object[]> findTestsTakenByMonth();

    @Query("SELECT t.test.id, count(t.takeId) from Take t group by t.test order by count(t.takeId)")
    List<Object[]> findTop10MostTakenTest();

    @Query("SELECT COUNT(DISTINCT t.user) FROM Take t")
    int countDistinctUsers();
}
