package com.makersacademy.acebook.repository;

import com.makersacademy.acebook.DTOs.DTOPostUserJoin;
import com.makersacademy.acebook.model.Post;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface PostRepository extends CrudRepository<Post, Long> {
    List<Post> findAllByOrderByDateOfPostDesc();

    @Query(
            value = "SELECT posts.id, posts.content, posts.image, posts.likes, posts.date_of_post, users.profile_picture, users.first_name, users.surname\n" +
                    "FROM posts LEFT JOIN users ON posts.poster = users.id;",
            nativeQuery = true
    )
    Iterable<DTOPostUserJoin> postsJoin();

}

