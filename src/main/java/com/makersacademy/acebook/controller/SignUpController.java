package com.makersacademy.acebook.controller;

import com.makersacademy.acebook.model.User;
import com.makersacademy.acebook.repository.PostRepository;
import jakarta.validation.constraints.Null;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.core.oidc.user.DefaultOidcUser;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.view.RedirectView;

@RestController
public class SignUpController {
    @Autowired
    PostRepository repository;

    @GetMapping("/sign_up")
    public ModelAndView signUp(){
        User user = new User();

        DefaultOidcUser principal = (DefaultOidcUser) SecurityContextHolder
                .getContext()
                .getAuthentication()
                .getPrincipal();

        String firstName = principal.getAttributes().get("given_name").toString();
        String surname = principal.getAttributes().get("family_name").toString();
        String emailAddress = principal.getAttributes().get("email").toString();

        ModelAndView signUp = new ModelAndView("/sign-up");
        signUp.addObject("sign_up", user);
        signUp.addObject("firstName", firstName);
        signUp.addObject("surname", surname);
        signUp.addObject("emailAddress", emailAddress);

        return signUp;
    }

    @PostMapping("/sign_up/new")
    public RedirectView create(User user) {
        DefaultOidcUser principal = (DefaultOidcUser) SecurityContextHolder
                .getContext()
                .getAuthentication()
                .getPrincipal();
        if (user.getUsername().isBlank()){
            return new RedirectView("/sign_up");
        }
        return new RedirectView("/posts");
    }

}
