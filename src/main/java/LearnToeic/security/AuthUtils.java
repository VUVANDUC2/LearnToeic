package LearnToeic.security;

import java.security.Principal;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

import LearnToeic.entity.User;
import LearnToeic.repository.AccountUserRepository;
import LearnToeic.security.CustomOAuth2User;
import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class AuthUtils {

    private final AccountUserRepository userRepo;

    public Integer currentUserId() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth == null) {
            throw new IllegalStateException("User not found");
        }

        String email = resolveEmail(auth);

        return userRepo.findByEmail(email)
                .map(User::getUserId)
                .orElseThrow(() -> new IllegalStateException("User not found"));
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
