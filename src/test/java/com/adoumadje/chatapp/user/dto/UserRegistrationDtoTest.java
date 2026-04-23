package com.adoumadje.chatapp.user.dto;

import com.adoumadje.chatapp.message.dto.MessageDto;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import tools.jackson.databind.ObjectMapper;

import java.util.Set;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

class UserRegistrationDtoTest {

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

    @Test
    void testUserRegistrationDto_WhenPasswordMismatch_ThenError() {
        json = """
                {
                  "username": "jdoe",
                  "firstname": "John",
                  "lastname": "Doe",
                  "email": "john.doe@example.com",
                  "profilePictureUrl": "https://example.com/images/jdoe.png",
                  "password": "Secure@124",
                  "confirmPassword": "Secure@123"
                }
                """;
        expectedViolationMessage = "Passwords do not match";
        UserRegistrationDto userRegistrationDto = objectMapper.readValue(json, UserRegistrationDto.class);
        Set<ConstraintViolation<UserRegistrationDto>> violations = validator.validate(userRegistrationDto);

        // Assertions
        Assertions.assertFalse(violations.isEmpty());
        Assertions.assertTrue(violations.stream()
                .anyMatch(violation ->
                        expectedViolationMessage.equals(violation.getMessage())));
    }
}