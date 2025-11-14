package LearnToeic.controller.web;

import java.security.Principal;

import org.springframework.beans.factory.annotation.Autowired;
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
}
