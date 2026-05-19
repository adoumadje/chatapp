package com.adoumadje.chatapp.user.service;

import com.adoumadje.chatapp.common.dto.ResponseDto;
import com.adoumadje.chatapp.user.dto.UserDto;
import com.adoumadje.chatapp.user.dto.UserRegistrationDto;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.jwt.Jwt;

import java.security.Principal;
import java.util.List;

public interface IUserService {
    List<UserDto> findUsers(Authentication authentication, String keyword, Integer pageNumber);

    ResponseDto registerUser(UserRegistrationDto userRegistrationDto);

    UserDto getOrCreateUser(Authentication authentication);
}
