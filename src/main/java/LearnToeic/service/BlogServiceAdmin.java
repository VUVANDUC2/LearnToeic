package LearnToeic.service;
import org.springframework.stereotype.Service;
import LearnToeic.repository.*;
import LearnToeic.entity.*;

import java.time.LocalDateTime;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
@Service
public class BlogServiceAdmin {

    @Autowired
    private BlogRepositoryAdmin blogRepo;

    public List<BlogPost> getAllBlogPosts() {
        return blogRepo.findAll();
    }

    public BlogPost getBlogPostById(Long id) {
        return blogRepo.findById(id).orElse(null);
    }

    public Page<BlogPost> getPaginateBlogPosts(String searchTerm, int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        if (searchTerm != null && !searchTerm.trim().isEmpty()) {
            return blogRepo.findByTitleContainingIgnoreCase(searchTerm, pageable);
        }
        return blogRepo.findAll(pageable);
    }

    public BlogPost saveBlogPost(String title, String content) {
        BlogPost b = new BlogPost();
        b.setTitle(title);
        b.setContent(content);

        // Tự động create slug
        b.setSlug(title.trim().toLowerCase().replace(" ", "-"));

        // Default values
        b.setCreatedAt(LocalDateTime.now());
        b.setCategory("General");
        
        b.setExcerpt(null);

        return blogRepo.save(b);
    }

    public void updateBlogPostContent(String title, String content, Long id) {
        blogRepo.updateBlogPost(title, content, id);
    }
}

