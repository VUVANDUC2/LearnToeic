package LearnToeic.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
public class SecurityConfig {
    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
            .authorizeHttpRequests(auth -> auth
                // Static
                .requestMatchers("/css/**","/js/**","/images/**","/webjars/**").permitAll()

                // Trang công khai
                .requestMatchers("/", "/auth/login", "/auth/signup", "/search", "/playlist", "/error").permitAll()

                // 🔒 BẮT đúng URL BẮT ĐẦU BÀI (đặt TRƯỚC rule /tests/**)
                .requestMatchers(HttpMethod.GET, "/tests/*/start").authenticated()

                // 👀 Còn lại các trang tests/lessons GET thì xem công khai
                .requestMatchers(HttpMethod.GET, "/tests/**", "/lessons/**").permitAll()

                // Các thao tác POST cần đăng nhập
                .requestMatchers(HttpMethod.POST, "/tests/**", "/attempts/**", "/progress/**", "/profile/**").authenticated()

                // Những URL khác tuỳ bạn:
                .anyRequest().authenticated()
            )

            .formLogin(login -> login
                .loginPage("/auth/login").permitAll()
                .loginProcessingUrl("/auth/login")
                .usernameParameter("email")
                .passwordParameter("password")
                // ĐỂ Spring quay về SavedRequest (URL /tests/{id}/start sau khi login)
                .defaultSuccessUrl("/", false)
            )

            .logout(lo -> lo.logoutUrl("/auth/logout").logoutSuccessUrl("/").permitAll());

        return http.build();
    }
}
