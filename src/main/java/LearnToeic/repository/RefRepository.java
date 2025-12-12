package LearnToeic.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import LearnToeic.entity.Ref;

public interface RefRepository extends JpaRepository<Ref, Integer>{
    List<Ref> findByTest_TestIdOrderByStartAsc(Integer testId);
}
