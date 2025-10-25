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

}
