package com.makersacademy.acebook.controller;

import com.makersacademy.acebook.model.User;
import com.makersacademy.acebook.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@Controller
public class SearchController {
    @Autowired
    private UserRepository userRepository;

    @GetMapping("/search")
    public String searchResults(@RequestParam String userSearch, Model model)  {
        List<User> results = userRepository.searchUsers(userSearch);
        model.addAttribute("searchResults", results);
        return "friends/search_results";

    }

}

