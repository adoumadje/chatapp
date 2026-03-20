package com.adoumadje.chatapp.message.dto;

import com.adoumadje.chatapp.message.enums.MessageAction;
import com.adoumadje.chatapp.message.enums.MessageTarget;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.UUID;

@Data
public class MessageDto {
    private MessageTarget messageTarget;
    private MessageAction messageAction;
    private String messageText;
    private String messageImageUrl;
    @NotNull(message = "Sender ID is mandatory")
    private UUID senderId;
    @NotNull(message = "receiver ID is mandatory")
    private UUID receiverId;
    private LocalDateTime timeStamp;
}
