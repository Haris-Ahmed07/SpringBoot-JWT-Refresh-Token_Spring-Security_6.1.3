package com.jwt.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToOne;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Data
@Entity
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private int uID;

    // The username of the user
    private String username;

    // The password of the user
    private String password;

    // The role of the user (e.g., admin, user)
    private String role;


    // A one-to-one mapping with the RefreshToken entity
    // The RefreshToken associated with this user (JsonIgnore is used to prevent circular serialization)
    @JsonIgnore
    @OneToOne(mappedBy = "user")
    private RefreshToken refreshToken;

    public User(String username, String password, String role) {
        super();
        this.username = username;
        this.password = password;
        this.role = role;
    }
}
