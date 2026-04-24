package com.adoumadje.chatapp.user.service.implementation;

import com.adoumadje.chatapp.common.dto.ResponseDto;
import com.adoumadje.chatapp.common.utils.Constants;
import com.adoumadje.chatapp.user.dto.UserDto;
import com.adoumadje.chatapp.user.dto.UserRegistrationDto;
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
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.test.context.bean.override.mockito.MockitoBean;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class UserServiceImplTest {
    @MockitoBean
    private UserRepository userRepository;
    @MockitoBean
    private KafkaTemplate<String, Object> kafkaTemplate;
    @Autowired
    private IUserService userService;


    @BeforeEach
    void setUp() {
    }

    @DisplayName("Find When keyword")
    @Test
    void testFindUsers_WhenKeyword_ThenList() {

        Mockito.when(userRepository.findByEmail(Mockito.anyString())).thenReturn(signleDbUser());
        Mockito.when(userRepository.searchUsers(Mockito.any(), Mockito.anyString(), Mockito.any()))
                .thenReturn(pageOfUsers());

        List<UserDto> userDtos = userService.findUsers(()-> "John", "Kyl", 1);

        // Assertions
        Assertions.assertFalse(userDtos.isEmpty());
    }

    @DisplayName("Find When no keyword")
    @Test
    void testFindUsers_WhenNoKeyword_ThenList() {

        Mockito.when(userRepository.findByEmail(Mockito.anyString())).thenReturn(signleDbUser());
        Mockito.when(userRepository.findByIdNot(Mockito.any(), Mockito.any()))
                .thenReturn(pageOfUsers());

        List<UserDto> userDtos = userService.findUsers(()-> "John", null, 1);

        // Assertions
        Assertions.assertFalse(userDtos.isEmpty());
    }

    @DisplayName("register user")
    @Test
    void testRegisterUser_WhenGoodInput_ThenOk() {
        Mockito.when(userRepository.save(Mockito.any(ChatUser.class))).thenReturn(saveUser());

        ResponseDto responseDto = userService.registerUser(createUserRegistrationDto());

        // Assertions
        Assertions.assertEquals(Constants.STATUS_ACCEPTED, responseDto.code());
        Assertions.assertEquals(Constants.USER_REGISTRATION_MSG, responseDto.message());
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

    private UserRegistrationDto createUserRegistrationDto() {
        UserRegistrationDto userRegistrationDto = new UserRegistrationDto();

        userRegistrationDto.setUsername("jdoe");
        userRegistrationDto.setFirstname("John");
        userRegistrationDto.setLastname("Doe");
        userRegistrationDto.setEmail("john.doe@example.com");
        userRegistrationDto.setProfilePictureUrl("https://example.com/images/jdoe.png");
        userRegistrationDto.setPassword("Secure@124");
        userRegistrationDto.setConfirmPassword("Secure@124");

        return userRegistrationDto;
    }

    private ChatUser saveUser() {
        ChatUser chatUser = new ChatUser();

        chatUser.setUsername("jdoe");
        chatUser.setFirstname("John");
        chatUser.setLastname("Doe");
        chatUser.setEmail("john.doe@example.com");
        chatUser.setProfilePictureUrl("https://example.com/images/jdoe.png");

        return chatUser;
    }
}