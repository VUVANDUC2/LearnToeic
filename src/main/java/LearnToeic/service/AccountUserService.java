package LearnToeic.service;

import LearnToeic.dto.auth.SignupRequest;
import LearnToeic.entity.User;
import LearnToeic.repository.AccountUserRepository;
import LearnToeic.service.exception.DuplicateEmailException;
import LearnToeic.service.exception.PasswordNotMatchException;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;

import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

@Service
@RequiredArgsConstructor
public class AccountUserService {
    private final AccountUserRepository userRepo;
    private final PasswordEncoder passwordEncoder;
    @Transactional
    public void signup(SignupRequest req) {
        String fullName = req.getFullName() != null ? req.getFullName().trim() : "";
        String email = req.getEmail() != null ? req.getEmail().trim().toLowerCase() : "";
        String password = req.getPassword() != null ? req.getPassword().trim() : "";
        String confirmPassword = req.getConfirmPassword() != null ? req.getConfirmPassword().trim() : "";

        if (!StringUtils.hasText(fullName) ||
            !StringUtils.hasText(email) ||
            !StringUtils.hasText(password) ||
            !StringUtils.hasText(confirmPassword)) {
            throw new IllegalArgumentException("Vui lòng nhập đầy đủ thông tin bắt buộc.");
        }

        if (userRepo.existsByEmail(email)) {
            throw new DuplicateEmailException("Email đã tồn tại");
        }
        if (!password.equals(confirmPassword)) {
            throw new PasswordNotMatchException("Mật khẩu xác nhận không khớp");
        }

        User u = new User();
        u.setFullName(fullName);
        u.setEmail(email);
        u.setPassword(passwordEncoder.encode(password));
        u.setRole("STUDENT"); // gán mặc định

        try {
            userRepo.save(u);
        } catch (DataIntegrityViolationException ex) {
            throw new DuplicateEmailException("Email đã tồn tại");
        }
    }
    public User fetchUserByEmail(String email) {
        return userRepo.findByEmail(email)
                .orElse(null);
    }

    @Transactional
    public void updateProfile(User user, String fullName, Boolean gender,
                              byte[] avatarData, String avatarContentType) {
        if (user == null) return;

        if (fullName != null && !fullName.isBlank()) {
            user.setFullName(fullName.trim());
        }
        user.setGender(gender);
        if (avatarData != null && avatarData.length > 0) {
            user.setAvatarData(avatarData);
            user.setAvatarContentType(avatarContentType);
        }

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
