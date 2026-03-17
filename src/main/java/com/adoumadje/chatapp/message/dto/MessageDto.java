package com.adoumadje.chatapp.message.dto;

import com.adoumadje.chatapp.message.enums.MessageType;
import lombok.Getter;
import lombok.Setter;

import java.util.UUID;

@Getter @Setter
public class MessageDto {
    private MessageType messageType;
    private String messageText;
    private String messageImageUrl;
    private UUID senderId;
    private UUID receiverId;
}
