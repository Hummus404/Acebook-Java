package com.makersacademy.acebook.controller;

import org.springframework.ui.Model;
import com.makersacademy.acebook.model.User;
import com.makersacademy.acebook.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.core.oidc.user.DefaultOidcUser;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;



@Controller
public class ProfileController {
    @Autowired private UserRepository userRepository;

    @GetMapping("/profile")
    public String profile(Model model) {

        DefaultOidcUser principal = (DefaultOidcUser) SecurityContextHolder
                .getContext()
                .getAuthentication()
                .getPrincipal();

        String emailAddress = (String) principal.getAttributes().get("email");

        User user = userRepository.findUserByEmailAddress(emailAddress).orElseThrow(() -> new RuntimeException("User not found"));
        System.out.println("USER IS: ");
        System.out.println(user);
        model.addAttribute("user", user);
        model.addAttribute("userName", user.getUsername());

        return "profile";
    }
}
