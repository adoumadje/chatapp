package com.adoumadje.chatapp.message.dto;

import com.adoumadje.chatapp.common.dto.ResponseDto;
import com.adoumadje.chatapp.message.enums.MessageType;

public record ErrorMessageDto(MessageType messageType, String username, ResponseDto error) {
}
