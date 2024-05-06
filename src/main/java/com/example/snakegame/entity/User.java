package com.example.snakegame.entity;

import jakarta.persistence.*;
import lombok.*;

import java.io.Serializable;


@Entity
@Getter
@Setter
@Table(name = "users")
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode
public class User implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    private String login;

    private String email;

    private String password;

    private long rating;



    public User(String login, String email, String password, long rating) {
        this.login = login;
        this.email = email;
        this.password = password;
        this.rating = rating;
    }
}
