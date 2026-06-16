package com.makersacademy.acebook.repository;

import com.makersacademy.acebook.DTOs.DTOcommentUserJoin;
import com.makersacademy.acebook.model.Comment;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import java.util.List;
import java.util.Optional;

public interface CommentRepository extends CrudRepository<Comment, Long> {

    Iterable<Comment> findAllByPost(Integer post);

    @Query(
            value = "SELECT comments.id, comments.content, comments.post, comments.date_of_comment, users.first_name, users.surname, users.profile_picture FROM comments LEFT JOIN users on comments.poster = users.id;",
            nativeQuery = true
    )
    Iterable<DTOcommentUserJoin> commentsJoin();

}







