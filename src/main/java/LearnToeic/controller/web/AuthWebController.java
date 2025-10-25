package LearnToeic.controller.web;

import LearnToeic.dto.auth.SignupRequest;
import LearnToeic.service.AccountUserService;
import LearnToeic.service.exception.DuplicateEmailException;
import LearnToeic.service.exception.PasswordNotMatchException;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthWebController {

    private final AccountUserService accountUserService;

    @GetMapping("/login")
    public String loginPage(
            @RequestParam(value = "error", required = false) String error,
            @RequestParam(value = "logout", required = false) String logout,
            Model model
    ) {
        if (error != null)  model.addAttribute("error", "Sai tài khoản hoặc mật khẩu");
        if (logout != null) model.addAttribute("msg", "Bạn đã đăng xuất");
        return "auth/login"; // templates/auth/login.html
    }

    @GetMapping("/signup")
    public String signupPage(Model model) {
        if (!model.containsAttribute("form")) {
            model.addAttribute("form", new SignupRequest());
        }
        return "auth/signup";
    }

    @PostMapping("/signup")
    public String handleSignup(@Valid @ModelAttribute("form") SignupRequest form,
                            BindingResult binding,
                            RedirectAttributes ra) {

        if (binding.hasErrors()) {
        binding.reject("formError","Vui lòng kiểm tra lại các trường đã nhập");
        return "auth/signup";
    }

        try {
            accountUserService.signup(form);
        } catch (DuplicateEmailException e) {
            binding.rejectValue("email", "duplicate", "Email đã được sử dụng");
            return "auth/signup";
        } catch (PasswordNotMatchException e) {
            binding.rejectValue("confirmPassword", "mismatch", "Mật khẩu xác nhận không khớp");
            return "auth/signup";
        }

        ra.addFlashAttribute("msg", "Đăng ký thành công, vui lòng đăng nhập.");
        return "redirect:/auth/login"; // phải /auth/login nếu controller dùng @RequestMapping("/auth")
    }

}

