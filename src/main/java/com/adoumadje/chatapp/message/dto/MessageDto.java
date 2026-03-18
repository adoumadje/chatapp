package com.adoumadje.chatapp.message.dto;

import com.adoumadje.chatapp.message.enums.MessageType;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.util.UUID;

@Data
public class MessageDto {
    private MessageType messageType;
    private String messageText;
    private String messageImageUrl;
    @NotNull(message = "Sender ID is mandatory")
    private UUID senderId;
    @NotNull(message = "receiver ID is mandatory")
    private UUID receiverId;
}
