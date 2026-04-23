package com.adoumadje.chatapp.user.entity;

import com.adoumadje.chatapp.common.entity.BaseEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import lombok.Getter;
import lombok.Setter;

import java.util.UUID;

@Entity
@Getter @Setter
public class ChatUser extends BaseEntity {
    @Column(unique = true, updatable = false)
    private String username;
    private String firstname;
    private String lastname;
    @Column(unique = true, updatable = false)
    private String email;
    private String profilePictureUrl;
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID mailBoxId;
}
