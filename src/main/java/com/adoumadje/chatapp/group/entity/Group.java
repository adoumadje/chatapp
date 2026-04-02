package com.adoumadje.chatapp.group.entity;

import com.adoumadje.chatapp.chat.entity.Chat;
import com.adoumadje.chatapp.common.entity.BaseEntity;
import com.adoumadje.chatapp.user.entity.ChatUser;
import lombok.Getter;
import lombok.Setter;

import java.util.List;
import java.util.UUID;

@Getter @Setter
public class Group extends BaseEntity {
    private Chat chat;
    private List<ChatUser> groupMembers;
    private UUID mailBoxId;
}
