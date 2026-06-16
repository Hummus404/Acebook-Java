package com.makersacademy.acebook.controller;

import com.makersacademy.acebook.DTOs.DTOCommentUserJoin;
import com.makersacademy.acebook.DTOs.DTOPostUserJoin;
import com.makersacademy.acebook.model.*;
import com.makersacademy.acebook.repository.CommentRepository;
import com.makersacademy.acebook.repository.LikeRepository;
import com.makersacademy.acebook.repository.PostRepository;
import com.makersacademy.acebook.repository.UserRepository;
import jakarta.servlet.http.HttpSession;
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
    PostRepository postRepository;

    @Autowired
    UserRepository userRepository;

    @Autowired
    LikeRepository likeRepository;

    @Autowired
    CommentRepository commentRepository;

    private User currentUser() {
        var auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth == null || !(auth.getPrincipal() instanceof DefaultOidcUser oidc)) return null;
        String email = (String) oidc.getAttributes().get("email");
        return userRepository.findUserByEmailAddress(email).orElse(null);
    }

    @GetMapping("/posts")
    public String index(Model model, Comment comment, HttpSession session) {
        Iterable<Post> posts = postRepository.findAllByOrderByDateOfPostDesc();
        User me = currentUser();
        List<PostView> postViews = new ArrayList<>();
        for (DTOPostUserJoin post : postRepository.postsJoin()) {
            long count = likeRepository.countByPostId(post.getId());
            boolean liked = me != null &&
                    likeRepository.existsByPostIdAndUserId(post.getId(), me.getId());
            postViews.add(new PostView(post, count, liked));
        }

        model.addAttribute("postViews", postViews);
        model.addAttribute("post", new Post());
        model.addAttribute("posts", posts);
        model.addAttribute("comment", comment);
        model.addAttribute("commentRepository", commentRepository);

//        Iterable<DTOCommentUserJoin>  commentUserJoin = commentRepository.commentsJoin();

//        model.addAttribute("commentUserJoin", commentUserJoin);

        session.setAttribute("userID", me.getId());

        return "posts/index";
    }


    @PostMapping("/posts/new")
    public RedirectView create(@ModelAttribute Post post, @RequestParam("imageFile") MultipartFile image, HttpSession session) throws IOException {

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


        post.setPoster((int) (long) session.getAttribute("userID"));
        post.setDateOfPost(LocalDateTime.now());

        postRepository.save(post);

        return new RedirectView("/posts");


    }

    @PostMapping("/posts")
    public RedirectView create(@ModelAttribute Post post) {
        post.setDateOfPost(LocalDateTime.now());
        postRepository.save(post);
        return new RedirectView("/posts");
    }

    @PostMapping("/posts/{id}/like")
    public RedirectView like(@PathVariable Long id) {
        User me = currentUser();

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