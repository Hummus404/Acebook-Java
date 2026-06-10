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
import org.springframework.web.servlet.view.RedirectView;

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

    @GetMapping("/posts")
    public String index(Model model) {
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
        return "post/index";
    }

    @PostMapping("/posts")
    public RedirectView create(@ModelAttribute Post post) {
        repository.save(post);
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

    private User currentUser() {
        var auth = SecurityContextHolder.getContext().getAuthentication();
            if (auth == null || !(auth.getPrincipal() instanceof
                    DefaultOidcUser oidc)) return null;
    }

}
