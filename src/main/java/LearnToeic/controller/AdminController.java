package LearnToeic.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@Controller
public class AdminController {
    @GetMapping("/admin")
    public String admin(Model model) {
        model.addAttribute("pageTitle", "Quản trị");
        return "admin/admin"; 
    }

    @GetMapping("/admin/test")
    public String test(Model model) {
        model.addAttribute("pageTitle", "Quản trị");
        return "admin/test"; 
    }

    @GetMapping("/admin/blog")
    public String blog(Model model) {
        model.addAttribute("pageTitle", "Quản trị");
        return "admin/blog"; 
    }

    @GetMapping("/admin/document")
    public String document(Model model) {
        model.addAttribute("pageTitle", "Quản trị");
        return "admin/document"; 
    }


    @GetMapping("/admin/test_transcript")
    public String testTranscript(Model model) {
        model.addAttribute("pageTitle", "Đáp án");
        return "admin/test_transcript"; 
    }

    @GetMapping("/admin/test_page/{testId}")
    public String testPage(@PathVariable Integer testId, Model model) {
        
        model.addAttribute("testId", testId); 

        model.addAttribute("pageTitle", "Trang kiểm tra: " + testId);
        
        return "admin/test_page"; 
    }

    @GetMapping("/admin/test_creation")
    public String testCreation(Model model) {
        model.addAttribute("pageTitle", "Trang kiểm tra");
        return "admin/test_create"; 
    }
}
