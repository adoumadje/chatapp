package com.adoumadje.chatapp.user.controller;

import com.adoumadje.chatapp.common.dto.ResponseDto;
import com.adoumadje.chatapp.user.dto.UserDto;
import com.adoumadje.chatapp.user.dto.UserRegistrationDto;
import com.adoumadje.chatapp.user.service.IUserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
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

    @PostMapping
    public ResponseEntity<ResponseDto> registerUser(@Validated @RequestBody UserRegistrationDto userRegistrationDto) {
        ResponseDto responseDto = iUserService.registerUser(userRegistrationDto);
        return new ResponseEntity<>(responseDto, HttpStatus.ACCEPTED);
    }
}
