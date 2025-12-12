package LearnToeic.controller.web;

import LearnToeic.entity.BlogPost;
import LearnToeic.service.BlogPostService;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class BlogWebController {

    private final BlogPostService blogPostService;

    public BlogWebController(BlogPostService blogPostService) {
        this.blogPostService = blogPostService;
    }

    @GetMapping("/blog")
    public String list(@RequestParam(name = "page", defaultValue = "1") int page,
                       Model model) {
        int pageSize = 6;
        int safePage = Math.max(page, 1);
        Page<BlogPost> postsPage = blogPostService.listPage(safePage - 1, pageSize);
        model.addAttribute("posts", postsPage);
        model.addAttribute("currentPage", postsPage.getNumber() + 1);
        model.addAttribute("totalPages", postsPage.getTotalPages());
        model.addAttribute("pageSize", pageSize);
        model.addAttribute("totalElements", postsPage.getTotalElements());
        return "/blog/blog-list";
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
        return "/blog/blog-detail";
    }
}
