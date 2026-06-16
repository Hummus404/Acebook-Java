package com.makersacademy.acebook.controller;

import com.makersacademy.acebook.model.Post;
import com.makersacademy.acebook.repository.PostRepository;
import org.springframework.ui.Model;
import com.makersacademy.acebook.model.User;
import com.makersacademy.acebook.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.core.oidc.user.DefaultOidcUser;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.List;


@Controller
public class ProfileController {
    @Autowired private UserRepository userRepository;
    @Autowired
    private PostRepository postRepository;

    @GetMapping("/profile")
    public String profile(Model model) {

        DefaultOidcUser principal = (DefaultOidcUser) SecurityContextHolder
                .getContext()
                .getAuthentication()
                .getPrincipal();

        String emailAddress = (String) principal.getAttributes().get("email");

        User user = userRepository.findUserByEmailAddress(emailAddress).orElseThrow(() -> new RuntimeException("User not found"));
        model.addAttribute("user", user);
        model.addAttribute("userName", user.getUsername());

        List<Post> posts = postRepository.findByPosterOrderByDateOfPostDesc(user.getId().intValue());

        model.addAttribute("posts", posts);
//        System.out.println("POST COUNT: " + posts.size());
//        posts.forEach(p -> System.out.println(p.getPoster()));
//        System.out.println("USER ID: " + user.getId());

        return "profile";
    }
}
