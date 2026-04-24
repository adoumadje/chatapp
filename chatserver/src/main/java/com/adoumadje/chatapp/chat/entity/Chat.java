package com.adoumadje.chatapp.chat.entity;

import com.adoumadje.chatapp.common.entity.BaseEntity;
import com.adoumadje.chatapp.message.entity.Message;
import com.adoumadje.chatapp.user.entity.ChatUser;

import java.util.List;

public class Chat extends BaseEntity {
    List<ChatUser> chatMembers;
    List<Message> chatMessages;
}
