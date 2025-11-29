package LearnToeic.controller.web;

import LearnToeic.entity.BlogPost;
import LearnToeic.service.BlogPostService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@Controller
public class BlogWebController {

    private final BlogPostService blogPostService;

    public BlogWebController(BlogPostService blogPostService) {
        this.blogPostService = blogPostService;
    }

    @GetMapping("/blog")
    public String list(Model model) {
        model.addAttribute("posts", blogPostService.listAll());
        return "blog-list";
    }

    @GetMapping("/blog/{slug}")
    public String detail(@PathVariable String slug, Model model) {
        BlogPost post = blogPostService.findBySlug(slug).orElse(null);
        if (post == null) {
            return "redirect:/blog";
        }
        model.addAttribute("post", post);
        model.addAttribute("relatedPosts", blogPostService.listAll().stream()
                .filter(p -> !p.getSlug().equals(slug))
                .limit(3)
                .toList());
        return "blog-detail";
    }
}
