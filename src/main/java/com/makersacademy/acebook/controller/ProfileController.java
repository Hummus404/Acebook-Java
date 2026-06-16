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

    @GetMapping("/profile/{username}")
    public String profile(@PathVariable String username, Model model) {

        User user = userRepository.findUserByUsername(username).orElseThrow(() -> new RuntimeException("User not found"));
        model.addAttribute("user", user);

        List<Post> posts = postRepository.findByPosterOrderByDateOfPostDesc(user.getId().intValue());

        model.addAttribute("posts", posts);

        return "profile";
    }
}
