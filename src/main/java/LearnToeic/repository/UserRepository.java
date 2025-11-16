package LearnToeic.repository;

import LearnToeic.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
//import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Integer> {
    Page<User> findByFullNameContainingIgnoreCase(String fullname, Pageable pageable);

}
