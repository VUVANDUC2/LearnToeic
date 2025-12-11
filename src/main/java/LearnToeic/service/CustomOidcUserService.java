package LearnToeic.service;

import org.springframework.stereotype.Service;
import org.springframework.security.oauth2.client.oidc.userinfo.OidcUserService;

import java.net.URL;
import java.util.UUID;
import java.io.InputStream;
import java.io.ByteArrayOutputStream;

import org.apache.commons.io.IOUtils; // cần thêm lib Apache Commons IO

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.client.oidc.userinfo.OidcUserRequest;
import org.springframework.security.oauth2.core.oidc.user.OidcUser;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;

import LearnToeic.entity.User;
import LearnToeic.repository.UserRepository;
import LearnToeic.security.CustomOAuth2User;

@Service
public class CustomOidcUserService extends OidcUserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public CustomOidcUserService(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }
    @Override
    public OidcUser loadUser(OidcUserRequest userRequest) throws OAuth2AuthenticationException {
        OidcUser oidcUser = super.loadUser(userRequest);

        String email = oidcUser.getAttribute("email");
        String name  = oidcUser.getAttribute("name");
        String pictureUrl = oidcUser.getAttribute("picture");

        // Tải ảnh từ URL thành byte[] (nếu có)
        byte[] avatarBytes = downloadAvatar(pictureUrl);

        User user = userRepository.findByEmail(email).orElseGet(() -> {
            User u = new User();
            u.setEmail(email);
            u.setFullName(name);
            u.setGender(null);

            String randomPwd = UUID.randomUUID().toString();
            u.setPassword(passwordEncoder.encode(randomPwd));
            u.setRole("STUDENT");

            if (avatarBytes != null) {
                u.setAvatarData(avatarBytes);
            }

            return userRepository.save(u);
        });

        boolean needUpdate = false;

        if (name != null && !name.equals(user.getFullName())) {
            user.setFullName(name);
            needUpdate = true;
        }

        if (avatarBytes != null && !java.util.Arrays.equals(avatarBytes, user.getAvatarData())) {
            user.setAvatarData(avatarBytes);
            needUpdate = true;
        }

        if (needUpdate) userRepository.save(user);

        return new CustomOAuth2User(oidcUser, user);
    }
    private byte[] downloadAvatar(String url) {
        if (url == null) return null;

        try (java.io.InputStream in = new java.net.URL(url).openStream()) {
            return in.readAllBytes();
        } catch (Exception e) {
            return null;
        }
    }

}
