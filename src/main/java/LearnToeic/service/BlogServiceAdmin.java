package LearnToeic.service;
import org.springframework.stereotype.Service;
import LearnToeic.repository.*;
import LearnToeic.model.*;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
@Service
public class BlogServiceAdmin {
    @Autowired
    private BlogRepositoryAdmin blogRepository;
    // Get all blogs
    public List<BlogAdmin> getAllBlogs() {
        return blogRepository.findAll();
    }

    // Get blog by ID
    public BlogAdmin getBlogById(int id) {
        return blogRepository.findById(id).orElse(null);
    }
    // Paginate blogs with optional search term
    public Page<BlogAdmin> getPaginateBlogs(String searchTerm,int page, int size)
    {   
        Pageable pageable = PageRequest.of(page, size);
        if(searchTerm != null && !searchTerm.trim().isEmpty())
        {    
            return blogRepository.findByTitleContainingIgnoreCase(searchTerm, pageable);
        }
        return blogRepository.findAll(pageable);
    }

    //Save Blog
    public BlogAdmin saveBlog(String title, String content) {
        BlogAdmin blog = new BlogAdmin();
        blog.setBlogId(blogRepository.getMaxBlogId() + 1);
        blog.setUserId(1);
        blog.setUpVote(0);
        blog.setTitle(title);
        blog.setContent(content);
        return blogRepository.save(blog);
    }
    //Update content of blog
    public void updateBlogContent(String title, String content, int blogId){
        blogRepository.updateBlog(title, content, blogId);
    }
}
