package com.adoumadje.chatapp.user.service;

import com.adoumadje.chatapp.common.dto.ResponseDto;
import com.adoumadje.chatapp.user.dto.UserDto;
import com.adoumadje.chatapp.user.dto.UserRegistrationDto;

import java.security.Principal;
import java.util.List;

public interface IUserService {
    List<UserDto> findUsers(Principal principal, String keyword, Integer pageNumber);

    ResponseDto registerUser(UserRegistrationDto userRegistrationDto);
}
