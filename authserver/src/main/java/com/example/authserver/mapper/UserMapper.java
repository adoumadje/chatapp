package com.example.authserver.mapper;

import com.adoumadje.chatapp.core.events.UserRegisteredEvent;
import com.example.authserver.entity.AppUser;
import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public interface UserMapper {
    AppUser toAppUser(UserRegisteredEvent userRegisteredEvent);
}
