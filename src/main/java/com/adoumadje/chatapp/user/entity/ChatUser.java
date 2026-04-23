package com.adoumadje.chatapp.user.entity;

import com.adoumadje.chatapp.common.entity.BaseEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import lombok.Getter;
import lombok.Setter;

import java.util.UUID;

@Entity
@Getter @Setter
public class ChatUser extends BaseEntity {
    @Column(unique = true)
    private String username;
    private String firstname;
    private String lastname;
    @Column(unique = true)
    private String email;
    private String profilePictureUrl;
    private UUID mailBoxId;
}
