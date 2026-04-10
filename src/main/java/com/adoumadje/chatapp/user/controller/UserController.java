package com.adoumadje.chatapp.user.controller;

import com.adoumadje.chatapp.user.dto.UserDto;
import com.adoumadje.chatapp.user.service.IUserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("api/v1/users")
@RequiredArgsConstructor
public class UserController {
    private final IUserService iUserService;

    @GetMapping
    public ResponseEntity<List<UserDto>> findUsers() {
        List<UserDto> users =
        return null;
    }
}
