package com.makersacademy.acebook.DTOs;


import lombok.Getter;

import java.sql.Timestamp;

@Getter
public class DTOPostUserJoin {

    private Long id;
    private String content;
    private String image;
    private Integer likes;
    private Timestamp date_of_post;
    private String profile_picture;
    private String first_name;
    private String surname;
    private String username;

    public DTOPostUserJoin(Long id, String content, String image, Integer likes, Timestamp date_of_post, String profile_picture, String first_name, String surname, String username){
        this.id = id;
        this.content = content;
        this.image = image;
        this.likes = likes;
        this.date_of_post = date_of_post;
        this.profile_picture = profile_picture;
        this.first_name = first_name;
        this.surname=surname;
        this.username=username;
    }

}


