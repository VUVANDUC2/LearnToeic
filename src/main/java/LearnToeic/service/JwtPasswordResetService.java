package LearnToeic.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import LearnToeic.entity.User;
import LearnToeic.repository.UserRepository;
import LearnToeic.security.JwtResetTokenUtil;

@Service
public class JwtPasswordResetService {
    @Autowired private UserRepository userRepo;
    @Autowired private JwtResetTokenUtil jwtUtil;
    @Autowired private PasswordEncoder passwordEncoder;
    @Autowired private EmailService  emailService;

    public void sendResetLink(String email, String siteURL) {
        User user = userRepo.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("Email không tồn tại"));

        String token = jwtUtil.generateResetToken(user.getEmail(), 15); // 15 phút
        String link = siteURL + "/auth/reset-password?token=" + token;

        String content = """
                Xin chào,
                Nhấn vào liên kết sau để đặt lại mật khẩu (hiệu lực 15 phút):
                %s
                """.formatted(link);

        emailService.sendEmail(email, "Đặt lại mật khẩu", content);
    }

    public boolean resetPassword(String token, String newPassword) {
        try {
            String email = jwtUtil.validateAndGetEmail(token);
            User user = userRepo.findByEmail(email)
                    .orElseThrow(() -> new RuntimeException("User không tồn tại"));

            user.setPassword(passwordEncoder.encode(newPassword));
            userRepo.save(user);
            return true;
        } catch (Exception e) {
            // token hết hạn, sai chữ ký, sai type...
            return false;
        }
    }
}
