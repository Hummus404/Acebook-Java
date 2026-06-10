package com.makersacademy.acebook.model;

import jakarta.persistence.*;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import static java.lang.Boolean.TRUE;

@Data
@Entity
@NoArgsConstructor
@Getter
@Setter
@Table(name = "USERS")
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String username;
    private boolean enabled;
    private String profile_picture;
    private String first_name;
    private String surname;
    @Column (name = "EMAIL_ADDRESS")
    private String emailAddress;
    private String friends;
    private String blocked;






    public User(String username) {
        this.username = username;
        this.enabled = TRUE;
    }

    public User(String username, boolean enabled, String profile_picture,String first_name, String surname, String emailAddress, String friends, String blocked) {
        this.username = username;
        this.enabled = enabled;
        this.profile_picture = profile_picture;
        this.first_name = first_name;
        this.surname = surname;
        this.emailAddress = emailAddress;
        this.friends = friends;
        this.blocked = blocked;
    }
}