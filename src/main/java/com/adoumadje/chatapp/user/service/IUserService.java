package com.adoumadje.chatapp.user.service;

import com.adoumadje.chatapp.user.dto.UserDto;

import java.security.Principal;
import java.util.List;

public interface IUserService {
    List<UserDto> findUsers(Principal principal, String keyword, Integer pageNumber);
}
