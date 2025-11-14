package LearnToeic.controller.api;

import java.security.Principal;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import LearnToeic.entity.User;
import LearnToeic.service.AccountUserService;



@Controller
public class ProfileController {

    @Autowired
    private AccountUserService accountUserService;

    @GetMapping("/profile")
    public String profile (Model model, Principal principal)
    {
        if(principal == null)
        {
            return "redirect:/auth/login";
        }
        User user = accountUserService.fetchUserByEmail(principal.getName());
        model.addAttribute("user", user);
        return "profile";
    }
}
