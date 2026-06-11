package com.makersacademy.acebook.model;

import lombok.Data;

@Data
public class PostView {
    private final Post post;
//    private String image;
    private final long likeCount;
    private final boolean likedByMe;

}
