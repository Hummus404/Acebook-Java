package com.makersacademy.acebook.model;

import com.makersacademy.acebook.DTOs.DTOPostUserJoin;
import lombok.Data;

import java.util.List;

@Data
public class PostView {
    private final DTOPostUserJoin post;
    private final long likeCount;
    private final boolean likedByMe;
    public final List<CommentView> commentView;
}
