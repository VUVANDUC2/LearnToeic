package LearnToeic.repository;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;

import LearnToeic.model.*;
import org.springframework.stereotype.Repository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
@Repository
public interface BlogRepository extends JpaRepository<Blog, Integer> {
    //Get highest id + 1
    @Query("Select MAX(b.blogId) from Blog b")
    int getMaxBlogId();

    //Paging
    Page<Blog> findByTitleContainingIgnoreCase(String title, Pageable pageable);


    //Update title, content by id
    @Modifying
    @Transactional
    @Query("UPDATE Blog b SET b.title = :title, b.content = :content WHERE b.id = :id") 
    int updateBlog(@Param("title") String title, @Param("content") String content, @Param("id") Integer id);
}
