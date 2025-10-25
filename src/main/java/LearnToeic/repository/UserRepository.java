package LearnToeic.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import LearnToeic.entity.User;

public interface UserRepository extends JpaRepository<User, Integer> {
    
}
