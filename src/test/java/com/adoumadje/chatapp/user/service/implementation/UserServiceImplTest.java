package com.adoumadje.chatapp.user.service.implementation;

import com.adoumadje.chatapp.user.dto.UserDto;
import com.adoumadje.chatapp.user.entity.ChatUser;
import com.adoumadje.chatapp.user.repository.UserRepository;
import com.adoumadje.chatapp.user.service.IUserService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.test.context.bean.override.mockito.MockitoBean;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class UserServiceImplTest {
    @MockitoBean
    private UserRepository userRepository;
    @Autowired
    private IUserService userService;

    @BeforeEach
    void setUp() {
    }

    @DisplayName("When keyword")
    @Test
    void testFindUsers_WhenKeyword_ThenList() {

        Mockito.when(userRepository.findByEmail(Mockito.anyString())).thenReturn(signleDbUser());
        Mockito.when(userRepository.searchUsers(Mockito.any(), Mockito.anyString(), Mockito.any()))
                .thenReturn(pageOfUsers());

        List<UserDto> userDtos = userService.findUsers(()-> "John", "Kyl", 1);

        // Assertions
        Assertions.assertFalse(userDtos.isEmpty());
    }

    @DisplayName("When no keyword")
    @Test
    void testFindUsers_WhenNoKeyword_ThenList() {

        Mockito.when(userRepository.findByEmail(Mockito.anyString())).thenReturn(signleDbUser());
        Mockito.when(userRepository.findByIdNot(Mockito.any(), Mockito.any()))
                .thenReturn(pageOfUsers());

        List<UserDto> userDtos = userService.findUsers(()-> "John", null, 1);

        // Assertions
        Assertions.assertFalse(userDtos.isEmpty());
    }

    private Page<ChatUser> pageOfUsers() {
        ChatUser user1 = new ChatUser();
        user1.setEmail("vini@example.com");
        ChatUser user2 = new ChatUser();
        user2.setEmail("rodrygo@example.com");
        return new PageImpl<>(List.of(user1, user2));
    }

    private Optional<ChatUser> signleDbUser() {
        ChatUser chatUser = new ChatUser();
        chatUser.setEmail("LeKyks@example.com");
        return Optional.of(chatUser);
    }
}