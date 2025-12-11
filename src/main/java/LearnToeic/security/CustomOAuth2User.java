package LearnToeic.security;

import java.util.Collection;
import java.util.Map;
import org.springframework.security.core.GrantedAuthority;
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
    @Override public Collection<? extends GrantedAuthority> getAuthorities() { return oidcUser.getAuthorities(); }
    @Override
    public String getName() {
        // ensure Authentication#getName returns email so downstream lookups work
        return user.getEmail() != null ? user.getEmail() : oidcUser.getName();
    }
    @Override public OidcUserInfo getUserInfo() { return oidcUser.getUserInfo(); }
    @Override public OidcIdToken getIdToken() { return oidcUser.getIdToken(); }
}
