package com.makersacademy.acebook.model;


import jakarta.persistence.*;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Data
@Entity
@Table(name = "COMMENTS")
@Getter
@Setter
public class Comment {


    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String content;
    private Integer poster;
    private Integer post;
    private Integer likes;
    @Column(name = "DATE_OF_COMMENT")
    private LocalDateTime dateOfPost;

    public Comment(){}

    public Comment(String content){

        this.content = content;

    }

}
