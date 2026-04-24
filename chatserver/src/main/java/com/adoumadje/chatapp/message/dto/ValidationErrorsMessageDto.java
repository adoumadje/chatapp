package com.adoumadje.chatapp.message.dto;

import com.adoumadje.chatapp.common.dto.ValidationErrorDto;
import com.adoumadje.chatapp.message.enums.MessageType;

import java.util.List;

public record ValidationErrorsMessageDto(MessageType messageType, String username, List<ValidationErrorDto> validationErrors) {
}
