package com.makersacademy.acebook.controller;

import com.makersacademy.acebook.model.Like;
import com.makersacademy.acebook.model.Post;
import com.makersacademy.acebook.model.PostView;
import com.makersacademy.acebook.model.User;
import com.makersacademy.acebook.repository.LikeRepository;
import com.makersacademy.acebook.repository.PostRepository;
import com.makersacademy.acebook.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.core.oidc.user.DefaultOidcUser;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.view.RedirectView;
import java.time.LocalDateTime;


import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.ArrayList;
import java.util.List;

@Controller
public class PostsController {

    @Autowired
    PostRepository repository;

    @Autowired
    UserRepository userRepository;

    @Autowired
    LikeRepository likeRepository;

    private User currentUser() {
        var auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth == null || !(auth.getPrincipal() instanceof DefaultOidcUser oidc)) return null;
        String email = (String) oidc.getAttributes().get("email");
        return userRepository.findUserByEmailAddress(email).orElse(null);
    }

    @GetMapping("/posts")
    public String index(Model model) {
        Iterable<Post> posts = repository.findAllByOrderByDateOfPostDesc();
        User me = currentUser();
        List<PostView> postViews = new ArrayList<>();
        for (Post post : repository.findAll()) {
            long count = likeRepository.countByPostId(post.getId());
            boolean liked = me != null &&
                    likeRepository.existsByPostIdAndUserId(post.getId(), me.getId());
            postViews.add(new PostView(post, count, liked));
        }
        model.addAttribute("postViews", postViews);
        model.addAttribute("post", new Post());
        model.addAttribute("posts", posts);
        return "posts/index";
    }

    @PostMapping("/posts/new")
    public RedirectView create(@ModelAttribute Post post, @RequestParam("imageFile") MultipartFile image) throws IOException {

        // Could implement a try catch block at a later point

        Path uploadDir = Paths.get("images");
        Files.createDirectories(uploadDir);

        if (!image.isEmpty()) {
            String filename = System.currentTimeMillis() + "_" + image.getOriginalFilename();
            Path filePath = uploadDir.resolve(filename);
            Files.copy(
                    image.getInputStream(),
                    filePath,
                    StandardCopyOption.REPLACE_EXISTING
            );
            post.setImage(filename);
        }

        DefaultOidcUser principal = (DefaultOidcUser) SecurityContextHolder
                .getContext()
                .getAuthentication()
                .getPrincipal();

        String emailAddress = (String) principal.getAttributes().get("email");

        User user = userRepository.findUserByEmailAddress(emailAddress)
                .orElseThrow();

        Integer userId = user.getId().intValue();

        post.setPoster(userId);

        repository.save(post);

        return new RedirectView("/posts");


    }

    @PostMapping("/posts/{id}/like")
    public RedirectView like(@PathVariable Long id) {
        System.out.println("Hello K");
        User me = currentUser();
        System.out.println("Hello A");

        if (me != null) {

            likeRepository.findByPostIdAndUserId(id, me.getId()).ifPresentOrElse(
                    likeRepository::delete,
                    // already liked -> unlike
                    () -> likeRepository.save(new Like(me.getId(), id))
                    // not yet -> like
            );
        }

        return new RedirectView("/posts");
    }

}