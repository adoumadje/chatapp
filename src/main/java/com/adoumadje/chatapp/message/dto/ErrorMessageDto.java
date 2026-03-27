package com.adoumadje.chatapp.message.dto;

import com.adoumadje.chatapp.common.dto.ErrorResponseDto;
import com.adoumadje.chatapp.message.enums.MessageType;

public record ErrorMessageDto(MessageType messageType, String username, ErrorResponseDto error) {
}
