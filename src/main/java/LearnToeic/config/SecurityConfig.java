package LearnToeic.config;

import LearnToeic.service.CustomOidcUserService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.SavedRequestAwareAuthenticationSuccessHandler;
import org.springframework.web.servlet.FlashMap;
import org.springframework.web.servlet.FlashMapManager;
import org.springframework.web.servlet.support.RequestContextUtils;
import org.springframework.web.servlet.support.SessionFlashMapManager;

@Configuration
public class SecurityConfig {

    private final CustomOidcUserService customOidcUserService;

    public SecurityConfig(CustomOidcUserService customOidcUserService) {
        this.customOidcUserService = customOidcUserService;
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http.csrf(csrf -> csrf.ignoringRequestMatchers("/auth/login"));

        http
            .authorizeHttpRequests(auth -> auth
                .requestMatchers("/css/**", "/js/**", "/images/**", "/webjars/**", "/uploads/**").permitAll()
                .requestMatchers(HttpMethod.GET, "/blog/**").permitAll()
                .requestMatchers("/", "/auth/forgot-password", "/auth/login", "/auth/signup", "/search", "/playlist", "/error").permitAll()
                .requestMatchers(HttpMethod.GET, "/tests/*/start").authenticated()
                .requestMatchers(HttpMethod.GET, "/tests/**", "/lessons/**").permitAll()
                .requestMatchers(HttpMethod.POST, "/tests/**", "/take/**", "/attempts/**", "/progress/**", "/profile/**").authenticated()
                .anyRequest().authenticated()
            )
            .oauth2Login(oauth2 -> oauth2
              .loginPage("/auth/login")
              .userInfoEndpoint(userInfo -> userInfo.oidcUserService(customOidcUserService))
              .defaultSuccessUrl("/", true)
            )
            .formLogin(login -> login
                .loginPage("/auth/login").permitAll()
                .loginProcessingUrl("/auth/login")
                .usernameParameter("email")
                .passwordParameter("password")
                .successHandler((request, response, authentication) -> {
                    addFlashMessage(request, response, "Dang nhap thanh cong!", "success");
                    SavedRequestAwareAuthenticationSuccessHandler handler = new SavedRequestAwareAuthenticationSuccessHandler();
                    handler.setDefaultTargetUrl("/");
                    handler.setAlwaysUseDefaultTargetUrl(false);
                    handler.onAuthenticationSuccess(request, response, authentication);
                })
            )
            .logout(logout -> logout
                .logoutUrl("/logout")
                .logoutSuccessHandler((request, response, authentication) -> {
                    addFlashMessage(request, response, "Ban da dang xuat thanh cong!", "info");
                    response.sendRedirect("/");
                })
                .invalidateHttpSession(true)
                .clearAuthentication(true)
                .deleteCookies("JSESSIONID")
            );

        return http.build();
    }

    private void addFlashMessage(HttpServletRequest request, HttpServletResponse response,
                                 String message, String alertClass) {
        FlashMap flashMap = RequestContextUtils.getOutputFlashMap(request);
        if (flashMap == null) {
            flashMap = new FlashMap();
        }
        flashMap.put("message", message);
        flashMap.put("alertClass", alertClass);

        FlashMapManager manager = RequestContextUtils.getFlashMapManager(request);
        if (manager == null) {
            manager = new SessionFlashMapManager();
        }
        manager.saveOutputFlashMap(flashMap, request, response);
    }
}
