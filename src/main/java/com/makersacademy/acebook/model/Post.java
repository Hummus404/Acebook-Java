package com.makersacademy.acebook.model;

import jakarta.persistence.*;

import lombok.Data;

import java.sql.Timestamp;

@Data
@Entity
@Table(name = "POSTS")
public class Post {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String content;
    private String image;
    @Column(name = "DATE_OF_POST")
    private Timestamp dateOfPost;
    private Integer poster;

    public Post() {}

    public Post(String content) {
        this.content = content;
    }

}
