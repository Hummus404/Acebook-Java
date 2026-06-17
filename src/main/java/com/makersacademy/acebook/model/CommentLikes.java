package com.makersacademy.acebook.model;

import jakarta.persistence.*;
import lombok.Data;

@Data
@Entity
@Table(name = "COMMENTLIKES")
public class CommentLikes {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "user_id")
    private Long userId;

    @Column(name = "comment_id")
    private Long commentId;

    public CommentLikes() {}

    public CommentLikes(Long userId, Long commentId) {
        this.userId = userId;
        this.commentId = commentId;
    }
}
