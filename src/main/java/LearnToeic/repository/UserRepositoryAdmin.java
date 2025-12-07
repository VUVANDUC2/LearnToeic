package LearnToeic.repository;

import LearnToeic.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

@Repository
public interface UserRepositoryAdmin extends JpaRepository<User, Integer> {
    Page<User> findByFullNameContainingIgnoreCase(String fullname, Pageable pageable);

    @Query("SELECT COUNT(DISTINCT u.userId) FROM User u")
    int countDistinctUsers();
}
