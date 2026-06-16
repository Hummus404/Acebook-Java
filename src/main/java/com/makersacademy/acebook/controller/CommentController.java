package com.makersacademy.acebook.controller;
import com.makersacademy.acebook.model.Comment;
import com.makersacademy.acebook.repository.CommentRepository;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.view.RedirectView;

import java.time.LocalDateTime;

@RestController
public class CommentController {

    @Autowired
    CommentRepository commentRepository;

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

}
