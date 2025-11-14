package LearnToeic.repository;


import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import LearnToeic.entity.User;

public interface AccountUserRepository extends JpaRepository<User, Integer>{
    boolean existsByEmail(String email);
    Optional<User>findByEmail(String email);
    boolean existsByFullName(String fullName);
}
