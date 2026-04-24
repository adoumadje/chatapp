package com.adoumadje.chatapp.common.exception;

import com.adoumadje.chatapp.common.utils.Constants;
import com.adoumadje.chatapp.user.controller.UserController;
import com.adoumadje.chatapp.user.dto.UserRegistrationDto;
import com.adoumadje.chatapp.user.entity.ChatUser;
import com.adoumadje.chatapp.user.service.IUserService;
import org.h2.engine.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.RequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import tools.jackson.databind.ObjectMapper;

import static org.junit.jupiter.api.Assertions.*;

@WebMvcTest(controllers = UserController.class)
class GlobalExceptionHandlerTest {

    @Autowired
    private MockMvc mockMvc;
    @MockitoBean
    private IUserService iUserService;
    @MockitoBean
    private SimpMessagingTemplate simpMessagingTemplate;

    @BeforeEach
    void setUp() {
    }

    @DisplayName("Resource Not Found")
    @Test
    void testHandleResourceNotFoundException() throws Exception {
        Mockito.when(iUserService.findUsers(Mockito.any(), Mockito.any(), Mockito.any()))

                .thenThrow(new ResourceNotFoundException(ChatUser.class.getSimpleName(), "email", "fake@example.com"));

        RequestBuilder requestBuilder = MockMvcRequestBuilders.get("/api/v1/users")
                .principal(() -> "John");

        mockMvc.perform(requestBuilder)
                .andExpect(MockMvcResultMatchers.status().isNotFound())
                .andExpect(MockMvcResultMatchers.jsonPath("$.code").value(Constants.STATUS_NOT_FOUND));
    }

    @DisplayName("Resource Already Exists")
    @Test
    void testHandleResourceAlreadyExistsException() throws Exception {
        Mockito.when(iUserService.registerUser(Mockito.any()))

                .thenThrow(new ResourceAlreadyExistsException(ChatUser.class.getSimpleName(), "email", "fake@example.com"));

        RequestBuilder requestBuilder = MockMvcRequestBuilders.post("/api/v1/users")
                .contentType("application/json")
                .content(new ObjectMapper().writeValueAsString(createUserRegistrationDto()));

        mockMvc.perform(requestBuilder)
                .andExpect(MockMvcResultMatchers.status().isBadRequest())
                .andExpect(MockMvcResultMatchers.jsonPath("$.code").value(Constants.STATUS_BAD_REQUEST));
    }

    @DisplayName("invalid dto")
    @Test
    void testHandleMethodArgumentNotValidException() throws Exception {
        RequestBuilder requestBuilder = MockMvcRequestBuilders.post("/api/v1/users")
                .contentType("application/json")
                .content(new ObjectMapper().writeValueAsString(createBadUserRegistrationDto()));

        mockMvc.perform(requestBuilder)
                .andExpect(MockMvcResultMatchers.status().isBadRequest())
                .andExpect(MockMvcResultMatchers.jsonPath("$").isArray());
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

    private UserRegistrationDto createBadUserRegistrationDto() {
        UserRegistrationDto userRegistrationDto = new UserRegistrationDto();

        userRegistrationDto.setUsername("");
        userRegistrationDto.setFirstname("John");
        userRegistrationDto.setLastname("Doe");
        userRegistrationDto.setEmail("john.doe@example.com");
        userRegistrationDto.setProfilePictureUrl("https://example.com/images/jdoe.png");
        userRegistrationDto.setPassword("Secure@124");
        userRegistrationDto.setConfirmPassword("Secure@123");

        return userRegistrationDto;
    }
}