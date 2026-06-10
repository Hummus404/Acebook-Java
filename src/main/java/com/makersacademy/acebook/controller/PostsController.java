package com.makersacademy.acebook.controller;

import com.makersacademy.acebook.model.Post;
import com.makersacademy.acebook.repository.PostRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.view.RedirectView;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.List;
import java.util.UUID;

@Controller
public class PostsController {

    @Autowired
    PostRepository repository;

    @GetMapping("/posts")
    public String index(Model model) {
        Iterable<Post> posts = repository.findAll();
        model.addAttribute("posts", posts);
        model.addAttribute("post", new Post());
        return "posts/index";
    }

    @PostMapping("/posts/new")
    public RedirectView create(@ModelAttribute Post post, @RequestParam("imageFile") MultipartFile image) throws IOException {

        // Could implement a try catch block at a later point

        Path uploadDir = Paths.get("images");
        Files.createDirectories(uploadDir);

        String filename = System.currentTimeMillis() + "_" + image.getOriginalFilename();

        Path filePath = uploadDir.resolve(filename);

        Files.copy(
                image.getInputStream(),
                filePath,
                StandardCopyOption.REPLACE_EXISTING
        );

        post.setImage(filename);
        System.out.println("HELLO");
        System.out.println(filePath.toString());

        repository.save(post);

        return new RedirectView("/posts");
    }
}
