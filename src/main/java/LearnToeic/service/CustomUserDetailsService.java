package LearnToeic.service;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import LearnToeic.entity.User;
import LearnToeic.repository.AccountUserRepository;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class CustomUserDetailsService implements UserDetailsService {

    private final AccountUserRepository userRepo;

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        User u = userRepo.findByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException("Email không tồn tại"));

        return org.springframework.security.core.userdetails.User
                .withUsername(u.getEmail())       // login dùng email
                .password(u.getPassword())        // password đã mã hoá
                .roles(u.getRole().replace("ROLE_", ""))
                .build();
    }
}

