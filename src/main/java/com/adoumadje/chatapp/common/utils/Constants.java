package com.adoumadje.chatapp.common.utils;

import org.springframework.http.HttpStatus;

public final class Constants {
    public static final int PAGE_SIZE = 20;

    public static final int STATUS_ACCEPTED = HttpStatus.ACCEPTED.value();
    public static final int STATUS_NOT_FOUND = HttpStatus.NOT_FOUND.value();

    public static final String USER_REGISTRATION_MSG = "User registered successfully";
}
