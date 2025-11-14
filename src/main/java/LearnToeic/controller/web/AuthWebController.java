package LearnToeic.controller.web;

import LearnToeic.dto.auth.SignupRequest;
import LearnToeic.entity.User;
import LearnToeic.repository.AccountUserRepository;
import LearnToeic.service.AccountUserService;
import LearnToeic.service.exception.DuplicateEmailException;
import LearnToeic.service.exception.PasswordNotMatchException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

import java.security.Principal;

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
            @RequestParam(value = "success", required = false) String success,
            Model model) {
        if (error != null) {
            model.addAttribute("message", "Tên đăng nhập hoặc mật khẩu không chính xác!");
            model.addAttribute("alertClass", "danger");
        }
        if (logout != null) {
            model.addAttribute("message", "Bạn đã đăng xuất thành công!");
            model.addAttribute("alertClass", "info");
        }
        if (success != null) {
            model.addAttribute("message", "Đăng nhập thành công!");
            model.addAttribute("alertClass", "success");
        }
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
        binding.reject("formError","Vui lòng kiểm tra lại các thông tin đã nhập đã nhập");
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

