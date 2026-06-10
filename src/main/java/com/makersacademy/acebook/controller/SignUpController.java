package com.makersacademy.acebook.controller;

import com.makersacademy.acebook.model.User;
import com.makersacademy.acebook.repository.PostRepository;
import com.makersacademy.acebook.repository.UserRepository;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.constraints.Null;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.core.oidc.user.DefaultOidcUser;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.view.RedirectView;

import java.util.Optional;

@RestController
public class SignUpController {
    @Autowired
    PostRepository repository;
    @Autowired
    UserRepository userRepository;

    @GetMapping("/sign_up")
    public ModelAndView signUp(HttpSession session){
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

        if (session.getAttribute("usernameBlank") != null){
            signUp.addObject("usernameBlank", true);
            session.setAttribute("usernameBlank", null);
        }

        if (session.getAttribute("uniqueUserBool") != null){
            signUp.addObject("uniqueUserBool", true);
            signUp.addObject("chosenUsername", session.getAttribute("chosenUsername"));
            session.setAttribute("uniqueUserBool", null);
        }

        return signUp;
    }

    @PostMapping("/sign_up/new")
    public RedirectView create(User user, HttpSession session) {
        DefaultOidcUser principal = (DefaultOidcUser) SecurityContextHolder
                .getContext()
                .getAuthentication()
                .getPrincipal();

        if (user.getUsername().isBlank()){
            Boolean usernameBlank = true;
            session.setAttribute("usernameBlank", usernameBlank);
            return new RedirectView("/sign_up");
        }

        Optional<User> uniqueUser = userRepository.findUserByUsername(user.getUsername());
        if (uniqueUser.isPresent()){
            String uniqueUserBool = user.getUsername();
            session.setAttribute("uniqueUserBool", uniqueUserBool);
            session.setAttribute("chosenUsername", uniqueUserBool);
            return new RedirectView("/sign_up");
        }

        if (user.getFirst_name().isBlank()){
            String firstName = principal.getAttributes().get("given_name").toString();
            user.setFirst_name(firstName);
        }

        if (user.getSurname().isBlank()) {
            String surname = principal.getAttributes().get("family_name").toString();
            user.setSurname(surname);
        }

        user.setEmailAddress(principal.getEmail());

        userRepository.save(user);

        return new RedirectView("/posts");
    }

}
