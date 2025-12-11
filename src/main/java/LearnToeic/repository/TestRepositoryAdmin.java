package LearnToeic.repository;

import LearnToeic.entity.Test;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.stereotype.Repository;

@Repository
public interface TestRepositoryAdmin extends JpaRepository<Test, Integer> {

    @Query("SELECT COALESCE(MAX(t.testId), 0) FROM Test t")
    int getMaxTestId();

    Page<Test> findByTestNameContainingIgnoreCase(String testName, Pageable pageable);

    // @Modifying
    // @Transactional
    // @Query("UPDATE Test t SET t.status = 'ARCHIVED' WHERE t.testId = :testId")
    // int updateStatusToArchived(Integer testId);
}
