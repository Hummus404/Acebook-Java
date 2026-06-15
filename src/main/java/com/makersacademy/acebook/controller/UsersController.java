package com.makersacademy.acebook.controller;

import com.makersacademy.acebook.model.User;
import com.makersacademy.acebook.repository.UserRepository;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.core.oidc.user.DefaultOidcUser;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.view.RedirectView;

import java.util.Optional;

@RestController
public class UsersController {
    @Autowired
    UserRepository userRepository;

    @GetMapping("/users/after-login")
    public RedirectView afterLogin(HttpSession session) {
        DefaultOidcUser principal = (DefaultOidcUser) SecurityContextHolder
                .getContext()
                .getAuthentication()
                .getPrincipal();

        String emailAddress = (String) principal.getAttributes().get("email");
//        userRepository
//                .findUserByUsername(username)
//                .orElseGet(() -> userRepository.save(new User(username)));

        Optional<User> uniqueUser = userRepository.findUserByEmailAddress(emailAddress);

        if (uniqueUser.isPresent()){

            session.setAttribute("profilePicture", uniqueUser.get().getProfilePicture());
            return new RedirectView("/posts");

        }
        else{
            return new RedirectView("/sign_up");
        }
    }

}
