package LearnToeic.controller.web;

import java.security.Principal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class HomeController {
    @GetMapping("/")
    public String home(Model model, Principal principal) {
        model.addAttribute("pageTitle", "Trang chủ • TOEIC Center");
        String name = (principal != null) ? principal.getName() : "Khách";
        model.addAttribute("displayName", name);
        return "Home";
    }


    @GetMapping("/test_page")
    public String test(Model model) {
        model.addAttribute("pageTitle", "Trang chủ • TOEIC Center");
        return "test_page";
    }

}


