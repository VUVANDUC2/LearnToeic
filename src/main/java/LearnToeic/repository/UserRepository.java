package LearnToeic.repository;

import LearnToeic.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

//import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Integer> {

    // You can add custom query methods here if needed, e.g.,
    // Optional<User> findByEmail(String email);
}
