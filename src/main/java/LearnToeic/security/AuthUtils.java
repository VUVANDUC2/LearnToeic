package LearnToeic.security;

import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

import LearnToeic.entity.User;
import LearnToeic.repository.AccountUserRepository;
import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class AuthUtils {

    private final AccountUserRepository userRepo;

    public Integer currentUserId() {
        var auth = SecurityContextHolder.getContext().getAuthentication();
        String email = auth.getName(); // vì .withUsername(u.getEmail())

        return userRepo.findByEmail(email)
                .map(User::getUserId)
                .orElseThrow(() -> new IllegalStateException("User not found"));
    }
}