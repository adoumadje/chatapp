package com.adoumadje.chatapp.common.utils;

import org.springframework.http.HttpStatus;

public final class Constants {
    public static final int PAGE_SIZE = 20;

    public static final int STATUS_ACCEPTED = HttpStatus.ACCEPTED.value();
    public static final int STATUS_NOT_FOUND = HttpStatus.NOT_FOUND.value();
    public static final int STATUS_BAD_REQUEST = HttpStatus.BAD_REQUEST.value();

    public static final String USER_REGISTRATION_MSG = "User registered successfully";

    public static final String GOOGLE_TOKEN_ISSUER = "";
    public static final String AUTH_SERVER_TOKEN_ISSUER = "http://localhost:8089";

}
