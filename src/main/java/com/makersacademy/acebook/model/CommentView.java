package com.makersacademy.acebook.model;

import com.makersacademy.acebook.DTOs.DTOCommentUserJoin;
import lombok.Data;

@Data
public class CommentView {
    private final DTOCommentUserJoin post;
    private final long likeCount;
    private final boolean likedByMe;
    private final long commentsId;
//
//    public CommentView(Long id){
//        this.commentsId = id;
//    }
//
//    public CommentView(DTOCommentUserJoin comments, long commentCount, boolean commentLiked, Long id) {
//    }
}


