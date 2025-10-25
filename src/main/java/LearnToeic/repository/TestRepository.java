package LearnToeic.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import LearnToeic.entity.Test;

public interface TestRepository extends JpaRepository<Test, Integer> {
    List<Test> findAllByOrderByTestDateDesc();
}
