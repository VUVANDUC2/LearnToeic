package LearnToeic.controller.web;

import java.util.Base64;
import java.security.Principal;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ModelAttribute;

import LearnToeic.entity.User;
import LearnToeic.service.AccountUserService;

@ControllerAdvice
public class GlobalModel {

    @Autowired
    private AccountUserService accountUserService;

    @ModelAttribute("currentUser")
    public User currentUser(Principal principal) {
        if (principal == null) return null;
        return accountUserService.fetchUserByEmail(principal.getName());
    }

    @ModelAttribute("avatarData")
    public String avatarData(Principal principal) {
        User user = currentUser(principal);
        if (user == null) return null;

        byte[] data = user.getAvatarData();
        String contentType = user.getAvatarContentType();
        if (data == null || data.length == 0 || !StringUtils.hasText(contentType)) {
            return null;
        }
        String base64 = Base64.getEncoder().encodeToString(data);
        return "data:" + contentType + ";base64," + base64;
    }

    @ModelAttribute("initial")
    public String initial(Principal principal) {
        User user = currentUser(principal);
        if (user == null) return "";

        String fullName = user.getFullName();
        if (StringUtils.hasText(fullName)) {
            return String.valueOf(fullName.trim().charAt(0)).toUpperCase();
        }
        String email = user.getEmail();
        if (StringUtils.hasText(email)) {
            return String.valueOf(email.trim().charAt(0)).toUpperCase();
        }
        return "";
    }
}
