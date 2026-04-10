package com.adoumadje.chatapp.user.entity;

import com.adoumadje.chatapp.common.entity.BaseEntity;
import lombok.Getter;
import lombok.Setter;

import java.util.UUID;

@Getter @Setter
public class ChatUser extends BaseEntity {
    private String username;
    private String firstname;
    private String lastname;
    private String email;
    private String profilePictureUrl;
    private UUID mailBoxId;
}
