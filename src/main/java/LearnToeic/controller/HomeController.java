package LearnToeic.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class HomeController {
    @GetMapping("/")
    public String home(Model model) {
        model.addAttribute("pageTitle", "Trang chủ • TOEIC Center");
        return "Home";  // templates/home.html
    }

    @GetMapping("/test_page")
    public String test(Model model) {
        model.addAttribute("pageTitle", "Trang chủ • TOEIC Center");
        return "test_page";
    }

}


