package com.makersacademy.acebook.repository;

import com.makersacademy.acebook.model.CommentLikes;
import com.makersacademy.acebook.model.Like;
import org.springframework.data.repository.CrudRepository;

import java.util.Optional;

public interface CommentLikeRepository extends CrudRepository <CommentLikes, Long> {
    long countByCommentId(long commentId); // displays count

    Optional<Like> findByCommentIdAndUserId(Long commentId, Long userID); // toggle implementation

    boolean existsByCommentIdAndUserId(Long commentId, Long userId); // button labelling


}
