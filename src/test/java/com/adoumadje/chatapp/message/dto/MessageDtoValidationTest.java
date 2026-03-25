package com.adoumadje.chatapp.message.dto;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import tools.jackson.databind.ObjectMapper;

import java.util.Set;
import java.util.UUID;

class MessageDtoValidationTest {

    private String json;
    private String expectedViolationMessage;
    private static Validator validator;
    private static ObjectMapper objectMapper;

    @BeforeAll
    static void setUp() {
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        validator = factory.getValidator();
        objectMapper = new ObjectMapper();
    }

    @DisplayName("Test No Sender Id Scenario")
    @Test
    void testMessageDto_WhenSenderIdNull_ThenViolation() {
        json = """
                {
                    "messageType": "PRIVATE",
                    "messageText": "Hello world!",
                    "messageImage": "",
                    "receiverId": "%s",
                    "chatId": "%s"
                }
                """.formatted(UUID.randomUUID(), UUID.randomUUID());
        expectedViolationMessage = "Sender ID is mandatory";
        MessageDto messageDto = objectMapper.readValue(json, MessageDto.class);
        Set<ConstraintViolation<MessageDto>> violations = validator.validate(messageDto);
        Assertions.assertFalse(violations.isEmpty());
        Assertions.assertTrue(violations.stream()
                .anyMatch(violation ->
                        expectedViolationMessage.equals(violation.getMessage())));
    }

    @DisplayName("Test No Receiver Id Scenario")
    @Test
    void testMessageDto_WhenReceiverIdNull_ThenException() {
        json = """
                {
                    "messageType": "PRIVATE",
                    "messageText": "Hello world!",
                    "messageImage": "",
                    "senderId": "%s",
                    "chatId": "%s"
                }
                """.formatted(UUID.randomUUID(), UUID.randomUUID());
        expectedViolationMessage = "Receiver ID is mandatory";
        MessageDto messageDto = objectMapper.readValue(json, MessageDto.class);
        Set<ConstraintViolation<MessageDto>> violations = validator.validate(messageDto);
        Assertions.assertFalse(violations.isEmpty());
        Assertions.assertTrue(violations.stream()
                .anyMatch(violation ->
                        expectedViolationMessage.equals(violation.getMessage())));
    }

    @DisplayName("Test No Chat Id Scenario")
    @Test
    void testMessageDto_WhenChatIdNull_ThenException() {
        json = """
                {
                    "messageType": "PRIVATE",
                    "messageText": "Hello world!",
                    "messageImage": "",
                    "senderId": "%s",
                    "receiverId": "%s"
                }
                """.formatted(UUID.randomUUID(), UUID.randomUUID());
        expectedViolationMessage = "Chat ID is mandatory";
        MessageDto messageDto = objectMapper.readValue(json, MessageDto.class);
        Set<ConstraintViolation<MessageDto>> violations = validator.validate(messageDto);
        Assertions.assertFalse(violations.isEmpty());
        Assertions.assertTrue(violations.stream()
                .anyMatch(violation ->
                        expectedViolationMessage.equals(violation.getMessage())));
    }
}