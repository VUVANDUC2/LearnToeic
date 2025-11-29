package LearnToeic.controller.web;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import LearnToeic.service.JwtPasswordResetService;
import jakarta.servlet.http.HttpServletRequest;

@Controller
@RequestMapping("/auth")
public class JwtPasswordController {

    @Autowired private JwtPasswordResetService resetService;

    @GetMapping("/forgot-password")
    public String showForgotPasswordForm() {
        return "auth/forgot-password"; // Tên file HTML
    }
    @PostMapping("/forgot-password")
    public String handleForgot(@RequestParam String email,
                               HttpServletRequest request,
                               RedirectAttributes redirect) {
        try {
            String siteURL = request.getRequestURL().toString()
                    .replace(request.getServletPath(), "");
            resetService.sendResetLink(email, siteURL);
        } catch (RuntimeException e) {
            // ẩn lỗi thực tế
        }
        redirect.addFlashAttribute("msg", "Nếu email tồn tại, liên kết đặt lại đã được gửi.");
        return "redirect:/auth/forgot-password";
    }

    @GetMapping("/reset-password")
    public String showResetForm(@RequestParam String token, Model model) {
        // Không check DB — chỉ truyền token sang view
        model.addAttribute("token", token);
        return "auth/reset-password";
    }

    @PostMapping("/reset-password")
    public String handleReset(@RequestParam String token,
                              @RequestParam String password,
                              RedirectAttributes redirect) {
        boolean ok = resetService.resetPassword(token, password);
        if (!ok) {
            redirect.addFlashAttribute("error", "Link không hợp lệ hoặc đã hết hạn.");
            return "redirect:/auth/reset-password?token=" + token;
        }
        redirect.addFlashAttribute("msg", "Đặt lại mật khẩu thành công.");
        return "redirect:/auth/login";
    }
}

