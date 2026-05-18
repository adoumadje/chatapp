package com.example.authserver.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Entity
@Getter @Setter @ToString
public class AppUser extends BaseEntity {
    @Column(unique = true, updatable = false)
    private String username;
    @Column(unique = true, updatable = false)
    private String email;
    private String password;
}
