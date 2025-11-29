package LearnToeic.repository;

import LearnToeic.entity.BlogPost;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface BlogPostRepository extends JpaRepository<BlogPost, Long> {
    boolean existsBySlug(String slug);
    Optional<BlogPost> findBySlug(String slug);
}
