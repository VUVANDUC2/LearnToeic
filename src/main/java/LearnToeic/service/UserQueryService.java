package LearnToeic.service;

import java.security.Principal;

import LearnToeic.entity.User;
import LearnToeic.repository.AccountUserRepository;
import LearnToeic.security.CustomOAuth2User;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserQueryService {

    private final AccountUserRepository userRepo;

    public Integer getCurrentUserId(Authentication auth) {
        if (auth == null) {
            throw new IllegalStateException("Không xác định được người dùng đăng nhập");
        }

        String email = resolveEmail(auth);
        User u = userRepo.findByEmail(email)
                .orElseThrow(() -> new IllegalStateException("Không tìm thấy user theo email: " + email));
        return u.getUserId();
    }

    private String resolveEmail(Authentication auth) {
        Object principal = auth.getPrincipal();

        if (principal instanceof CustomOAuth2User oauthUser && oauthUser.getUser() != null) {
            return oauthUser.getUser().getEmail();
        }
        if (principal instanceof org.springframework.security.core.userdetails.User userDetails) {
            return userDetails.getUsername();
        }
        if (principal instanceof Principal p) {
            return p.getName();
        }
        return auth.getName();
    }
}
