package LearnToeic.controller.web;

import LearnToeic.entity.BlogPost;
import LearnToeic.entity.BlogProgram;
import LearnToeic.service.BlogPostService;
import jakarta.validation.constraints.NotBlank;
import org.docx4j.openpackaging.exceptions.Docx4JException;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@Controller
@Validated
public class BlogAdminController {

    private final BlogPostService blogPostService;

    public BlogAdminController(BlogPostService blogPostService) {
        this.blogPostService = blogPostService;
    }

    @GetMapping("/admin/blog")
    public String adminList(Model model) {
        model.addAttribute("posts", blogPostService.listAll());
        return "admin/blog-manage";
    }

    @GetMapping("/admin/blog/new-docx")
    public String newDocxPage(Model model) {
        model.addAttribute("programs", BlogProgram.values());
        return "new-docx";
    }

    @PostMapping("/admin/blog/new-docx")
    public String uploadDocx(@RequestParam("file") MultipartFile file,
                             @RequestParam("title") @NotBlank String title,
                             @RequestParam("category") @NotBlank String category,
                             @RequestParam("program") BlogProgram program,
                             @RequestParam(value = "excerpt", required = false) String excerpt,
                             @RequestParam(value = "thumbnail", required = false) MultipartFile thumbnail,
                             Model model) {
        boolean hasDocx = file != null && !file.isEmpty();
        boolean hasManual = (excerpt != null && !excerpt.isBlank()) || (thumbnail != null && !thumbnail.isEmpty());
        if (!hasDocx && !hasManual) {
            model.addAttribute("error", "Chon file .docx HOAC nhập excerpt/thumbnail");
            model.addAttribute("programs", BlogProgram.values());
            return "new-docx";
        }
        try {
            BlogPost post;
            if (hasDocx) {
                post = blogPostService.createFromDocx(file, title, category, program, excerpt, thumbnail);
            } else {
                post = blogPostService.createManual(title, category, program, excerpt, thumbnail);
            }
            return "redirect:/blog/" + post.getSlug();
        } catch (IOException | Docx4JException e) {
            model.addAttribute("error", "Tai len that bai: " + e.getMessage());
            model.addAttribute("programs", BlogProgram.values());
            return "new-docx";
        }
    }

    @PostMapping("/admin/blog/delete/{id}")
    public String delete(@PathVariable Long id) {
        blogPostService.deleteById(id);
        return "redirect:/admin/blog?deleted";
    }
}
