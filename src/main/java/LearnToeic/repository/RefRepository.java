package LearnToeic.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import LearnToeic.entity.Ref;

public interface RefRepository extends JpaRepository<Ref, Integer>{
    
}
