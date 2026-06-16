package com.makersacademy.acebook.controller;

import com.makersacademy.acebook.model.Post;
import com.makersacademy.acebook.repository.FriendshipRepository;
import com.makersacademy.acebook.repository.PostRepository;
import org.springframework.ui.Model;
import com.makersacademy.acebook.model.Friendship;
import com.makersacademy.acebook.model.User;
import com.makersacademy.acebook.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.core.oidc.user.DefaultOidcUser;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.List;
import java.util.Optional;


@Controller
public class ProfileController {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PostRepository postRepository;

    @Autowired
    FriendshipRepository friendshipRepository;

    private User currentUser() {
        var auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth == null || !(auth.getPrincipal() instanceof DefaultOidcUser oidc)) return null;
        String email = (String) oidc.getAttributes().get("email");
        return userRepository.findUserByEmailAddress(email).orElse(null);
    }

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

        List<Post> posts = postRepository.findByPosterOrderByDateOfPostDesc(user.getId().intValue());

        model.addAttribute("posts", posts);

        return "profile";
    }

    @GetMapping("/profile/{username}")
    public String showUserProfile(@PathVariable String username, Model model) {
        User me = currentUser();
        User profileUser = userRepository.findUserByUsername(username).orElseThrow();

        model.addAttribute("profileUser", profileUser);

        if (me.getId().equals(profileUser.getId())) {
            model.addAttribute("isOwnProfile", true);
        } else {
            model.addAttribute("isOwnProfile", false);

            Optional<Friendship> sent = friendshipRepository
                    .findByRequesterAndAddressee(me, profileUser);
            Optional<Friendship> received = friendshipRepository
                    .findByRequesterAndAddressee(profileUser, me);

            if (sent.isPresent()) {
                model.addAttribute("friendshipStatus", sent.get().getStatus().toString());
            } else if (received.isPresent()) {
                model.addAttribute("friendshipStatus", "INCOMING_" + received.get().getStatus().toString());
                model.addAttribute("friendshipId", received.get().getId());
            } else {
                model.addAttribute("friendshipStatus", "NONE");
            }

            boolean isFriend = sent.isPresent() && sent.get().getStatus().toString().equals("ACCEPTED")
                    || received.isPresent() && received.get().getStatus().toString().equals("ACCEPTED");

            model.addAttribute("isFriend", isFriend);
        }

        return "users/profile";
    }

}
