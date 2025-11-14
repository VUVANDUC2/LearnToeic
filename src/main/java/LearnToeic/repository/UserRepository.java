package LearnToeic.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import LearnToeic.entity.User;

public interface UserRepository extends JpaRepository<User, Integer> {
    Optional<User> findByEmail(String email);

}
