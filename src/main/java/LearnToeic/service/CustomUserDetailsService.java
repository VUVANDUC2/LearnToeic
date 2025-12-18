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

        String role = u.getRole();
        if (role == null || role.isBlank()) role = "STUDENT";
        role = role.trim().toUpperCase();
        if (role.startsWith("ROLE_")) role = role.substring("ROLE_".length());

        return org.springframework.security.core.userdetails.User
                .withUsername(u.getEmail())       // login dùng email
                .password(u.getPassword())        // password đã mã hoá
                .roles(role)
                .build();
        //return new CustomUserDetails(u);
    }
}

