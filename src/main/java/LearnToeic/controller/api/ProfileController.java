package LearnToeic.controller.api;

import java.security.Principal;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import LearnToeic.dto.Exam.TakeHistoryVM;
import LearnToeic.entity.User;
import LearnToeic.service.AccountUserService;
import LearnToeic.service.ExamService;



@Controller
public class ProfileController {

    @Autowired
    private AccountUserService accountUserService;
    @Autowired
    private ExamService examService;

    @GetMapping("/profile")
    public String profile (Model model, Principal principal)
    {
        if(principal == null)
        {
            return "redirect:/auth/login";
        }
        User user = accountUserService.fetchUserByEmail(principal.getName());
        model.addAttribute("user", user);
        var history = examService.buildTakeHistory(user.getUserId());
        model.addAttribute("history", history);
        return "profile";
    }
    @PostMapping("/profile")
    public String updateProfile(@RequestParam("fullName") String fullName,
                                @RequestParam(value = "gender", required = false) Boolean gender,
                                Principal principal,
                                RedirectAttributes ra) {

        if (principal == null) {
            return "redirect:/auth/login";
        }

        if (fullName == null || fullName.isBlank()) {
            ra.addFlashAttribute("message", "Họ tên không được để trống.");
            ra.addFlashAttribute("alertClass", "danger");
            return "redirect:/profile";
        }

        User user = accountUserService.fetchUserByEmail(principal.getName());
        if (user == null) {
            ra.addFlashAttribute("message", "Không tìm thấy thông tin người dùng.");
            ra.addFlashAttribute("alertClass", "danger");
            return "redirect:/auth/login";
        }

        accountUserService.updateProfile(user, fullName, gender);

        ra.addFlashAttribute("message", "Cập nhật thông tin thành công!");
        ra.addFlashAttribute("alertClass", "success");
        return "redirect:/profile";
    }
}
