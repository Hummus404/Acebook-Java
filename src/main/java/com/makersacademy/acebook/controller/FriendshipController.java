package com.makersacademy.acebook.controller;

import org.springframework.ui.Model;
import com.makersacademy.acebook.model.Friendship;
import com.makersacademy.acebook.model.FriendshipStatus;
import com.makersacademy.acebook.model.User;
import com.makersacademy.acebook.repository.FriendshipRepository;
import com.makersacademy.acebook.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.core.oidc.user.DefaultOidcUser;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Controller
@RequestMapping("/friends")
public class FriendshipController {
    @Autowired
    private FriendshipRepository friendshipRepository;
    @Autowired
    private UserRepository userRepository;

    private User currentUser() {
        var auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth == null || !(auth.getPrincipal() instanceof DefaultOidcUser oidc)) return null;
        String email = (String) oidc.getAttributes().get("email");
        return userRepository.findUserByUsername(email).orElse(null);
    }

    @PostMapping("/request/{addresseeId}")
    public String sendRequest(@PathVariable Long addresseeId) {
        User requestor = currentUser();
        User addressee = userRepository.findById(addresseeId).orElseThrow();
        boolean alreadyExists = friendshipRepository
                .findByRequestorAndAddressee(requestor, addressee)
                .isPresent();
        if (!alreadyExists) {
            Friendship friendship = new Friendship();
            friendship.setRequestor(requestor);
            friendship.setAddressee(addressee);
            friendship.setStatus(FriendshipStatus.PENDING);
            friendship.setCreatedAt(LocalDateTime.now());
            friendshipRepository.save(friendship);
        }
        return "redirect:/users/" + addresseeId;
    }

    @PostMapping("/accept/{friendshipId}")
    public String accept(@PathVariable Long friendshipId) {
        Friendship friendship = friendshipRepository.findById(friendshipId).orElseThrow();
        friendship.setStatus(FriendshipStatus.ACCEPTED);
        friendshipRepository.save(friendship);
        return "redirect:/users/requests";
    }

    @PostMapping("/decline/{friendshipId}")
    public String decline(@PathVariable Long friendshipId) {
        Friendship f = friendshipRepository.findById(friendshipId).orElseThrow();
        f.setStatus(FriendshipStatus.DECLINED);
        friendshipRepository.save(f);
        return "redirect:/friends/requests";
    }

    @GetMapping("/requests")
    public String viewRequests(Model model) {
        User me = currentUser();
        model.addAttribute("pendingRequests",
                friendshipRepository.findByAddresseeAndStatus(me, FriendshipStatus.PENDING));
        return "friends/requests";
    }

    @GetMapping
    public String viewFriends(Model model) {
        User me = currentUser();

        List<Friendship> sent = friendshipRepository.findByRequestorAndStatus(me, FriendshipStatus.ACCEPTED);
        List<Friendship> received = friendshipRepository.findByAddresseeAndStatus(me, FriendshipStatus.ACCEPTED);

        List<User> friends = new ArrayList<>();
        sent.forEach(friendship -> friends.add(friendship.getAddressee()));
        received.forEach(friendship -> friends.add(friendship.getRequestor()));

        model.addAttribute("friends", friends);
        return "friends/list";
    }
}
