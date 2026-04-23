package com.adoumadje.chatapp.user.mapper;

import com.adoumadje.chatapp.core.events.UserRegisteredEvent;
import com.adoumadje.chatapp.user.dto.UserDto;
import com.adoumadje.chatapp.user.dto.UserRegistrationDto;
import com.adoumadje.chatapp.user.entity.ChatUser;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper(componentModel = "spring")
public interface UserMapper {

    UserDto toDto(ChatUser user);

    ChatUser toChatUser(UserRegistrationDto userRegistrationDto);

    List<UserDto> toDtoList(List<ChatUser> users);

    UserRegisteredEvent toUserRegisteredEvent(ChatUser user);

}
