package LearnToeic.controller.web.api;

import java.security.Principal;
import java.io.IOException;
import java.util.Base64;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.springframework.web.multipart.MultipartFile;

import LearnToeic.dto.Exam.TakeHistoryVM;
import LearnToeic.dto.Profile.ProfileDashboardVM;
import LearnToeic.entity.User;
import LearnToeic.security.JwtResetTokenUtil;
import LearnToeic.service.AccountUserService;
import LearnToeic.service.ExamService;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Controller
public class ProfileController {

    @Autowired
    private AccountUserService accountUserService;
    @Autowired
    private ExamService examService;
    @Autowired
    private JwtResetTokenUtil jwtResetTokenUtil;

    @GetMapping("/profile")
    public String profile(Model model, Principal principal) {
        if (principal == null) {
            return "redirect:/auth/login";
        }
        User user = accountUserService.fetchUserByEmail(principal.getName());
        if (user == null) {
            return "redirect:/auth/login";
        }
        model.addAttribute("user", user);
        model.addAttribute("avatarData", buildAvatarData(user));
        List<TakeHistoryVM> history = examService.buildTakeHistory(user.getUserId());
        model.addAttribute("history", history);
        model.addAttribute("dashboard", buildDashboard(history));
        String resetToken = jwtResetTokenUtil.generateResetToken(user.getEmail(), 15);
        model.addAttribute("resetToken", resetToken);
        return "profile/profile";
    }

    @PostMapping("/profile")
    public String updateProfile(@RequestParam("fullName") String fullName,
                                @RequestParam(value = "gender", required = false) Boolean gender,
                                @RequestParam(value = "avatar", required = false) MultipartFile avatar,
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

        byte[] avatarBytes = null;
        String avatarContentType = null;
        if (avatar != null && !avatar.isEmpty()) {
            if (avatar.getContentType() == null || !avatar.getContentType().startsWith("image/")) {
                ra.addFlashAttribute("message", "Chỉ hỗ trợ tải lên file ảnh (jpeg, png, ...).");
                ra.addFlashAttribute("alertClass", "danger");
                return "redirect:/profile";
            }
            if (avatar.getSize() > 2 * 1024 * 1024) {
                ra.addFlashAttribute("message", "Ảnh đại diện phải nhỏ hơn 2MB.");
                ra.addFlashAttribute("alertClass", "danger");
                return "redirect:/profile";
            }
            try {
                avatarBytes = avatar.getBytes();
                avatarContentType = avatar.getContentType();
            } catch (IOException e) {
                ra.addFlashAttribute("message", "Không thể đọc file ảnh. Vui lòng thử lại.");
                ra.addFlashAttribute("alertClass", "danger");
                return "redirect:/profile";
            }
        }

        accountUserService.updateProfile(user, fullName, gender, avatarBytes, avatarContentType);

        ra.addFlashAttribute("message", "Cập nhật thông tin thành công!");
        ra.addFlashAttribute("alertClass", "success");
        return "redirect:/profile";
    }

    private ProfileDashboardVM buildDashboard(List<TakeHistoryVM> history) {
        int total = history.size();
        List<Integer> scores = history.stream()
                .map(TakeHistoryVM::score)
                .filter(Objects::nonNull)
                .collect(Collectors.toList());

        int completedWithScore = scores.size();
        Double average = completedWithScore > 0
                ? scores.stream().mapToInt(Integer::intValue).average().orElse(0)
                : null;
        Integer best = completedWithScore > 0
                ? scores.stream().max(Integer::compareTo).orElse(null)
                : null;

        TakeHistoryVM latest = history.isEmpty() ? null : history.get(0);

        return new ProfileDashboardVM(
                total,
                completedWithScore,
                average,
                best,
                latest
        );
    }

    private String buildAvatarData(User user) {
        if (user == null) return null;
        byte[] data = user.getAvatarData();
        String contentType = user.getAvatarContentType();
        if (data == null || data.length == 0 || contentType == null) {
            return null;
        }
        String base64 = Base64.getEncoder().encodeToString(data);
        return "data:" + contentType + ";base64," + base64;
    }
}
