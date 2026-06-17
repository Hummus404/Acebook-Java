package com.makersacademy.acebook.controller;
import com.makersacademy.acebook.model.Comment;
import com.makersacademy.acebook.model.CommentLikes;
import com.makersacademy.acebook.model.Like;
import com.makersacademy.acebook.model.User;
import com.makersacademy.acebook.repository.CommentLikeRepository;
import com.makersacademy.acebook.repository.CommentRepository;
import com.makersacademy.acebook.repository.UserRepository;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.core.oidc.user.DefaultOidcUser;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.view.RedirectView;

import java.time.LocalDateTime;

@RestController
public class CommentController {

    @Autowired
    CommentRepository commentRepository;

    @Autowired
    CommentLikeRepository commentLikeRepository;

    @Autowired
    UserRepository userRepository;

    private User currentUser() {
        var auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth == null || !(auth.getPrincipal() instanceof DefaultOidcUser oidc)) return null;
        String email = (String) oidc.getAttributes().get("email");
        return userRepository.findUserByEmailAddress(email).orElse(null);
    }

    @PostMapping("/comment/new")
    public RedirectView commentRedirect(Comment comment, HttpSession session){

        if (comment.getContent().isEmpty()){
            return new RedirectView("/posts");
        } else {

            comment.setDateOfPost(LocalDateTime.now());
            comment.setPoster((int) (long) session.getAttribute("userID"));

        }

        System.out.println(comment.getPost());

        commentRepository.save(comment);

        return new RedirectView("/posts");
    }

    @PostMapping("/posts/{id}/comment-likes")
    public RedirectView commentLikes(@PathVariable Long id) {
        User me = currentUser();

        if (me != null) {

            commentLikeRepository.findByCommentIdAndUserId(id, me.getId()).ifPresentOrElse(
                    commentLikeRepository::delete,
                    // already liked -> unlike
                    () -> commentLikeRepository.save(new CommentLikes(me.getId(), id))
                    // not yet -> like
            );
        }

        return new RedirectView("/posts");
    }


}
