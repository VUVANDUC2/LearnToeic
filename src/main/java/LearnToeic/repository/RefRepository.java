package LearnToeic.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import LearnToeic.entity.Ref;
import LearnToeic.entity.RefId;

public interface RefRepository extends JpaRepository<Ref, RefId>{
    List<Ref> findByTest_TestIdOrderByStartAsc(Integer testId);
}
