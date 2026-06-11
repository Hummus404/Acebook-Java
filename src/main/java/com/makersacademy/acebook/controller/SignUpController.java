package com.makersacademy.acebook.controller;

import com.makersacademy.acebook.model.Post;
import com.makersacademy.acebook.model.User;
import com.makersacademy.acebook.repository.PostRepository;
import com.makersacademy.acebook.repository.UserRepository;
import jakarta.servlet.http.HttpSession;
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

        ModelAndView signUp = new ModelAndView("/sign-up");

        String emailAddress = principal.getAttributes().get("email").toString();

        Optional<User> uniqueUser = userRepository.findUserByEmailAddress(emailAddress);

        if (uniqueUser.isPresent()){
            ModelAndView model = new ModelAndView("posts/index");
            Iterable<Post> posts = repository.findAll();
            model.addObject("posts", posts);
            model.addObject("post", new Post());
            return model;
        }

        if (principal.getGivenName() != null) {
            String firstName = principal.getAttributes().get("given_name").toString();
            signUp.addObject("firstName", firstName);
        }
        else{
            signUp.addObject("firstName","Enter first name");
        }

        if (principal.getFamilyName() != null) {
            String surname = principal.getAttributes().get("family_name").toString();
            System.out.println(surname);
            signUp.addObject("surname", surname);
        }
        else{
            signUp.addObject("surname","Enter surname");
        }

        signUp.addObject("sign_up", user);
        signUp.addObject("emailAddress", emailAddress);

        if (session.getAttribute("usernameBlank") != null){
            signUp.addObject("usernameBlank", true);
            session.setAttribute("usernameBlank", null);
        }

        if (session.getAttribute("noFirstName") != null){
            signUp.addObject("noFirstName", true);
            session.setAttribute("noFirstName", null);
        }

        if (session.getAttribute("noSurname") != null){
            signUp.addObject("noSurname", true);
            session.setAttribute("noSurname", null);
        }

        if (session.getAttribute("bothNamesBlank") != null){
            signUp.addObject("bothNamesBlank", true);
            session.setAttribute("bothNamesBlank", null);
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

        if (user.getFirst_name().isBlank() && user.getSurname().isBlank()) {
            if (principal.getGivenName() != null) {
                String firstName = principal.getAttributes().get("given_name").toString();
                String surname = principal.getAttributes().get("family_name").toString();
                user.setFirst_name(firstName);
                user.setSurname(surname);
            }
            else {
                session.setAttribute("bothNamesBlank", true);
                return new RedirectView("/sign_up");
            }
        } else if (user.getFirst_name().isBlank()) {
            if (principal.getGivenName() != null) {
                String firstName = principal.getAttributes().get("given_name").toString();
                user.setFirst_name(firstName);
            }
            else {
                session.setAttribute("noFirstName", true);
                return new RedirectView("/sign_up");
            }
        }
        else {
            if (principal.getFamilyName() != null) {
                String surname = principal.getAttributes().get("family_name").toString();
                user.setSurname(surname);
            } else {
                session.setAttribute("noSurname", true);
                return new RedirectView("/sign_up");
            }
        }

        user.setEmailAddress(principal.getEmail());

        userRepository.save(user);

        return new RedirectView("/posts");
    }

}
