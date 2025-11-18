package LearnToeic.service;

import LearnToeic.dto.auth.SignupRequest;
import LearnToeic.entity.User;
import LearnToeic.repository.AccountUserRepository;
import LearnToeic.service.exception.DuplicateEmailException;
import LearnToeic.service.exception.DuplicateUsernameException;
import LearnToeic.service.exception.PasswordNotMatchException;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AccountUserService {
    private final AccountUserRepository userRepo;
    private final PasswordEncoder passwordEncoder;
    @Transactional
    public void signup(SignupRequest req) {
        if (userRepo.existsByEmail(req.getEmail())) {
            throw new DuplicateEmailException("Email đã tồn tại");
        }
        if (userRepo.existsByFullName(req.getFullName())) {
            throw new DuplicateUsernameException("Username đã tồn tại");
        }
        if (!req.getPassword().equals(req.getConfirmPassword())) {
            throw new PasswordNotMatchException("Mật khẩu xác nhận không khớp");
        }

        User u = new User();
        u.setFullName(req.getFullName());
        u.setEmail(req.getEmail());
        u.setPassword(passwordEncoder.encode(req.getPassword()));
        u.setRole("STUDENT"); // gán mặc định

        userRepo.save(u);
    }
    public User fetchUserByEmail(String email) {
        return userRepo.findByEmail(email)
                .orElse(null);
    }

    @Transactional
    public void updateProfile(User user, String fullName, Boolean gender) {
        if (user == null) return;

        if (fullName != null && !fullName.isBlank()) {
            user.setFullName(fullName.trim());
        }
        user.setGender(gender);

        userRepo.save(user);
    }

    // public void sendResetLink(String email, String siteURL) {
    //     User user = userRepo.findByEmail(email).orElse(null);
    //     if (user == null) throw new RuntimeException("Email không tồn tại!");

    //     String token = UUID.randomUUID().toString();
    //     PasswordResetToken resetToken = new PasswordResetToken();
    //     resetToken.setToken(token);
    //     resetToken.setEmail(email);
    //     resetToken.setExpiryDate(LocalDateTime.now().plusMinutes(30));

    //     tokenRepo.save(resetToken);

    //     String link = siteURL + "/reset_password?token=" + token;
    //     emailService.sendEmail(email, "Đặt lại mật khẩu",
    //         "Nhấn vào link để đặt lại mật khẩu: " + link);
    // }

}
