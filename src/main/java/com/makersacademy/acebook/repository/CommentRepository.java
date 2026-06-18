package com.makersacademy.acebook.repository;

import com.makersacademy.acebook.DTOs.DTOCommentUserJoin;
import com.makersacademy.acebook.model.Comment;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

public interface CommentRepository extends CrudRepository<Comment, Long> {

    Iterable<Comment> findAllByPost(Integer post);

    @Query(
            value = "SELECT comments.id, comments.content, comments.post, comments.date_of_comment, users.first_name, users.surname, users.profile_picture FROM comments LEFT JOIN users on comments.poster = users.id WHERE comments.post = :postID;",
            nativeQuery = true
    )
    Iterable<DTOCommentUserJoin> commentsJoin(@Param("postID") Integer postID);

    @Query(
            value = "SELECT comments.id, comments.content, comments.post, comments.date_of_comment, users.first_name, users.surname, users.profile_picture FROM comments LEFT JOIN users on comments.poster = users.id",
            nativeQuery = true
    )
    Iterable<DTOCommentUserJoin> commentsJoin();

}







