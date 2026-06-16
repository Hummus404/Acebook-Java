package com.makersacademy.acebook.DTOs;

import lombok.Getter;

import java.sql.Timestamp;
import java.time.LocalDateTime;

@Getter
public class DTOCommentUserJoin {

    private Long id;
    private String content;
    private Integer post;
    private Timestamp date_of_comment;
    private String first_name;
    private String surname;
    private String profile_picture;

    public DTOCommentUserJoin(Long id, String content, Integer post, Timestamp date_of_comment, String first_name, String surname, String profile_picture){
        this.id = id;
        this.content = content;
        this.post = post;
        this.date_of_comment = date_of_comment;
        this.first_name = first_name;
        this.surname = surname;
        this.profile_picture = profile_picture;
    }




}
