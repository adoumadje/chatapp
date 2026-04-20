package com.adoumadje.chatapp.user.controller;

import com.adoumadje.chatapp.user.dto.UserDto;
import com.adoumadje.chatapp.user.service.IUserService;
import org.junit.jupiter.api.Assertions;
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
import tools.jackson.core.type.TypeReference;
import tools.jackson.databind.ObjectMapper;

import java.util.List;
import java.util.UUID;

@WebMvcTest(controllers = UserController.class)
class UserControllerWebLayerTest {

    @Autowired
    private MockMvc mockMvc;
    @MockitoBean
    private IUserService iUserService;
    @MockitoBean
    private SimpMessagingTemplate simpMessagingTemplate;

    @BeforeEach
    void setUp() {
    }

    @DisplayName("find users when no param specified")
    @Test
    void testFindUsers_WhenNoQueryParam_ThenNormal() throws Exception {
        Mockito.when(iUserService.findUsers(Mockito.any(), Mockito.any(), Mockito.any()))

                .thenReturn(createUserDtoList());

        RequestBuilder requestBuilder = MockMvcRequestBuilders.get("/api/v1/users")
                .principal(() -> "John");

        MvcResult mvcResult = mockMvc.perform(requestBuilder)
                .andReturn();

        String reponseBodyAsString = mvcResult.getResponse().getContentAsString();

        List<UserDto> userDtos = new ObjectMapper().readValue(reponseBodyAsString,
                new TypeReference<List<UserDto>>() {});

        // Assertions

        Assertions.assertFalse(userDtos.isEmpty());
    }

    private List<UserDto> createUserDtoList() {
        return List.of(
                new UserDto("user215", "kylian", "Mbappe", "kyks@example.com",
                        "picture.jpg", UUID.randomUUID()),
                new UserDto("user1256", "Vini", "Junior", "kyks@example.com",
                        "picture.jpg", UUID.randomUUID())
        );
    }
}