package com.adoumadje.chatapp.user.mapper;

import com.adoumadje.chatapp.user.dto.UserDto;
import com.adoumadje.chatapp.user.entity.ChatUser;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper(componentModel = "spring")
public interface UserMapper {

    UserDto toDto(ChatUser user);

    List<UserDto> toDtoList(List<ChatUser> users);

}
