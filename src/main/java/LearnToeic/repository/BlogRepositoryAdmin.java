package LearnToeic.repository;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;

import LearnToeic.entity.*;
import org.springframework.stereotype.Repository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
@Repository
public interface BlogRepositoryAdmin extends JpaRepository<BlogPost, Long> {

    @Query("SELECT MAX(b.id) FROM BlogPost b")
    Long getMaxBlogPostId();

    Page<BlogPost> findByTitleContainingIgnoreCase(String title, Pageable pageable);

    @Modifying
    @Transactional
    @Query("UPDATE BlogPost b SET b.title = :title, b.content = :content WHERE b.id = :id")
    int updateBlogPost(@Param("title") String title,
                       @Param("content") String content,
                       @Param("id") Long id);
}

