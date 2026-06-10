package com.makersacademy.acebook.repository;

import com.makersacademy.acebook.model.Like;
import org.springframework.data.repository.CrudRepository;

import java.util.Optional;

public interface LikeRepository extends CrudRepository<Like, Long> {
    long countByPostId(long postId); // displays count

    Optional<Like> findByPostIdAndUserId(Long postId, Long userID); // toggle

    boolean existsByPostIdAndUserId(Long postId, Long userId); // button labelling


}
