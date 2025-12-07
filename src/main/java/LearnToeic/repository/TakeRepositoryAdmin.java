package LearnToeic.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import LearnToeic.entity.Take;
import java.util.List;

@Repository
public interface TakeRepositoryAdmin extends JpaRepository<Take, Integer> {

    @Query("SELECT t FROM Take t WHERE t.test.testId = :testId")
    List<Take> findAllByTestId(Integer testId);

    // @Query("SELECT t.test.testId, AVG(t.score) FROM Take t GROUP BY t.test.testId")
    // List<Object[]> findAverageScorePerTest();

    @Query(value = """
            SELECT user_id, COUNT(*) AS total_attempts
            FROM takes
            GROUP BY user_id
            ORDER BY total_attempts DESC
            LIMIT 5
            """, nativeQuery = true)
    List<Object[]> findUserAttemptsOrdered();

    @Query(value = """
            SELECT MONTH(taken_on) AS month, COUNT(*) 
            FROM takes 
            GROUP BY MONTH(taken_on)
            ORDER BY month
            """, nativeQuery = true)
    List<Object[]> findTestsTakenByMonth();

    @Query(value = """
            SELECT test_id, COUNT(*) 
            FROM takes 
            GROUP BY test_id
            ORDER BY COUNT(*) DESC
            LIMIT 10
            """, nativeQuery = true)
    List<Object[]> findTop10MostTakenTest();

    @Query("SELECT COUNT(DISTINCT t.user.userId) FROM Take t")
    int countDistinctUsers();
}
