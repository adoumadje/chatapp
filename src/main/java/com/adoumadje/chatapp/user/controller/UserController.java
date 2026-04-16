package com.adoumadje.chatapp.user.controller;

import com.adoumadje.chatapp.user.dto.UserDto;
import com.adoumadje.chatapp.user.service.IUserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.List;

@RestController
@RequestMapping("api/v1/users")
@RequiredArgsConstructor
public class UserController {
    private final IUserService iUserService;

    @GetMapping
    public ResponseEntity<List<UserDto>> findUsers(Principal principal, @RequestParam(required = false) String keyword,
                                                    @RequestParam(required = false) Integer pageNumber) {
        List<UserDto> users = iUserService.findUsers(principal, keyword, pageNumber);
        return new ResponseEntity<>(users, HttpStatus.OK);
    }
}
