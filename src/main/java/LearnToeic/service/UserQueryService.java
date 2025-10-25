package LearnToeic.service;

import LearnToeic.entity.User;
import LearnToeic.repository.AccountUserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserQueryService {

    private final AccountUserRepository userRepo;

    public Integer getCurrentUserId(Authentication auth) {
        // // Cách 1: nếu auth.getPrincipal() là CustomUserDetails có sẵn userId
        // if (auth != null && auth.getPrincipal() instanceof LearnToeic.security.CustomUserDetails cud) {
        //     return cud.getId();
        // }

        // Cách 2: nếu đang dùng username = email
        // auth.getName() trả về "username" của user → ở bạn là email
        String email = (auth != null) ? auth.getName() : null;
        if (email == null) throw new IllegalStateException("Không xác định được người dùng đăng nhập");

        User u = userRepo.findByEmail(email)
                .orElseThrow(() -> new IllegalStateException("Không tìm thấy user theo email: " + email));
        return u.getUserId(); // đổi theo tên cột khoá chính trong entity User
    }
}
