package LearnToeic.repository;


import LearnToeic.model.Test;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.transaction.annotation.Transactional;

@Repository
public interface TestRepository extends JpaRepository<Test, Integer> {
    @Query("Select MAX(t.testId) from Test t")
    int getMaxTestId();

    Page<Test> findByTestNameContainingIgnoreCase(String testName, Pageable pageable);
    @Modifying
    @Transactional
    @Query("UPDATE Test t SET t.status = 'ARCHIVED' WHERE t.testId = :testId") 
    int updateStatusToArchived(@Param("testId") Integer testId);
}