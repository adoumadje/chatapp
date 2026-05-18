package com.example.authserver.mapper;

import com.adoumadje.chatapp.core.events.UserRegisteredEvent;
import com.example.authserver.entity.AppUser;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingConstants;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public interface UserMapper {
    @Mapping(target = "username", source = "username")
    @Mapping(target = "email", source = "email")
    @Mapping(target = "password", source = "password")
    AppUser toAppUser(UserRegisteredEvent userRegisteredEvent);
}
