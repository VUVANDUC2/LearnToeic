package LearnToeic.security;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.core.oidc.OidcIdToken;
import org.springframework.security.oauth2.core.oidc.OidcUserInfo;
import org.springframework.security.oauth2.core.oidc.user.OidcUser;

import LearnToeic.entity.User;

public class CustomOAuth2User implements OidcUser {

    private final OidcUser oidcUser;
    private final User user;

    public CustomOAuth2User(OidcUser oidcUser, User user) {
        this.oidcUser = oidcUser;
        this.user = user;
    }

    // expose DB user so Thymeleaf có thể gọi ${#authentication.principal.user}
    public User getUser() {
        return user;
    }

    // delegate OidcUser methods
    @Override public Map<String, Object> getClaims() { return oidcUser.getClaims(); }
    @Override public Map<String, Object> getAttributes() { return oidcUser.getAttributes(); }
    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        List<GrantedAuthority> authorities = new ArrayList<>(oidcUser.getAuthorities());
        String normalizedRole = normalizeRole(user != null ? user.getRole() : null);
        if (normalizedRole != null) {
            String roleAuthority = "ROLE_" + normalizedRole;
            boolean exists = authorities.stream().anyMatch(a -> roleAuthority.equals(a.getAuthority()));
            if (!exists) {
                authorities.add(new SimpleGrantedAuthority(roleAuthority));
            }
        }
        return authorities;
    }
    @Override
    public String getName() {
        // ensure Authentication#getName returns email so downstream lookups work
        return user.getEmail() != null ? user.getEmail() : oidcUser.getName();
    }
    @Override public OidcUserInfo getUserInfo() { return oidcUser.getUserInfo(); }
    @Override public OidcIdToken getIdToken() { return oidcUser.getIdToken(); }

    private static String normalizeRole(String role) {
        if (role == null) return null;
        String normalized = role.trim();
        if (normalized.isEmpty()) return null;
        normalized = normalized.toUpperCase();
        if (normalized.startsWith("ROLE_")) normalized = normalized.substring("ROLE_".length());
        return normalized;
    }
}
