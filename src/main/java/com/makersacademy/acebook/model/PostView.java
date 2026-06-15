package com.makersacademy.acebook.model;

import com.makersacademy.acebook.DTOs.DTOPostUserJoin;
import lombok.Data;

@Data
public class PostView {
    private final DTOPostUserJoin post;
    private final long likeCount;
    private final boolean likedByMe;
}
