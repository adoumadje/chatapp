package com.adoumadje.chatapp.user.dto;

import java.util.UUID;

public record UserDto(String username, String firstname, String lastname, String email, String profilePictureUrl,
        UUID mailBoxId) {
}
