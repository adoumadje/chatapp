package com.adoumadje.chatapp.message.exception;

import com.adoumadje.chatapp.common.dto.ErrorResponseDto;
import com.adoumadje.chatapp.common.dto.ValidationErrorDto;
import com.adoumadje.chatapp.message.dto.ErrorMessageDto;
import com.adoumadje.chatapp.message.dto.ValidationErrorsMessageDto;
import com.adoumadje.chatapp.message.enums.MessageType;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.messaging.converter.MessageConversionException;
import org.springframework.messaging.handler.annotation.MessageExceptionHandler;
import org.springframework.messaging.handler.annotation.support.MethodArgumentNotValidException;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.web.bind.annotation.ControllerAdvice;

import java.util.List;

// Todo: add Principal object
@ControllerAdvice
@RequiredArgsConstructor
public class ChatMessageExceptionHandler {
    @Value("${broker.prefix.error-message}")
    private String errorMessagePrefix;

    private final SimpMessagingTemplate simpMessagingTemplate;

    @MessageExceptionHandler(MethodArgumentNotValidException.class)
    public void handleMessageDtoValidationException(MethodArgumentNotValidException exception) {
        String username = "default";
        assert exception.getBindingResult() != null;
        List<ValidationErrorDto> validationErrors = exception.getBindingResult()
                .getFieldErrors()
                .stream()
                .map(err -> new ValidationErrorDto(err.getField(), err.getDefaultMessage()))
                .toList();
        ValidationErrorsMessageDto messageDto = new ValidationErrorsMessageDto(MessageType.VALIDATION_ERRORS,
                username, validationErrors);
        simpMessagingTemplate.convertAndSend(errorMessagePrefix + "/" + username, messageDto);
    }

    @MessageExceptionHandler(MessageConversionException.class)
    public void handleMessageConversionException(MessageConversionException exception) {
        String username = "default";
        ErrorMessageDto errorMessageDto = new ErrorMessageDto(MessageType.ERROR, username,
                new ErrorResponseDto(HttpStatus.BAD_REQUEST.value(), exception.getMessage()));
        simpMessagingTemplate.convertAndSend(errorMessagePrefix + "/" + username, errorMessageDto);
    }

    @MessageExceptionHandler(WrongMessageTargetException.class)
    public void handleWrongMessageTargetException(WrongMessageTargetException exception) {
        String username = "default";
        ErrorMessageDto errorMessageDto = new ErrorMessageDto(MessageType.ERROR, username,
                new ErrorResponseDto(HttpStatus.BAD_REQUEST.value(), exception.getMessage()));
        simpMessagingTemplate.convertAndSend(errorMessagePrefix + "/" + username, errorMessageDto);
    }
}
