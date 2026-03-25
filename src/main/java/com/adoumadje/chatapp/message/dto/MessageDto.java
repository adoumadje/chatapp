package com.adoumadje.chatapp.message.dto;

import com.adoumadje.chatapp.message.enums.MessageType;
import com.adoumadje.chatapp.message.enums.MessageTarget;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.UUID;

@Data
public class MessageDto {
    private MessageTarget messageTarget;
    private MessageType messageType;
    private String messageText;
    private String messageImageUrl;
    @NotNull(message = "Sender ID is mandatory")
    private UUID senderId;
    @NotNull(message = "Receiver ID is mandatory")
    private UUID receiverId;
    @NotNull(message = "Chat ID is mandatory")
    private UUID chatId;
    private LocalDateTime timeStamp;
}
